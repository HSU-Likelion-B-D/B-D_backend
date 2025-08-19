package com.likelion.bd.domain.match.service;

import com.likelion.bd.domain.businessman.entity.*;
import com.likelion.bd.domain.influencer.entity.*;
import com.likelion.bd.domain.match.config.MatchProperties;
import com.likelion.bd.domain.match.repository.BusinessManMatchRepository;
import com.likelion.bd.domain.match.repository.InfluencerMatchRepository;
import com.likelion.bd.domain.match.util.DomainSimilarity;
import com.likelion.bd.domain.match.util.SimilarityUtils;
import com.likelion.bd.domain.match.util.StatsUtils;
import com.likelion.bd.domain.match.web.dto.RecommendInfluencerRes;
import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.global.exception.CustomException;
import com.likelion.bd.global.response.code.businessMan.BusinessManErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final BusinessManMatchRepository businessManMatchRepository;
    private final InfluencerMatchRepository influencerMatchRepository;
    private final MatchProperties props;

    @Override
    public List<RecommendInfluencerRes> top5ForBusinessMan(Long businessManId) {

        //자영업자 1명 검색
        BusinessMan businessMan = businessManMatchRepository.findById(businessManId)
                .orElseThrow(() -> new CustomException(BusinessManErrorResponseCode.BUSINESSMAN_NOT_FOUND_404));

        // 2) 사업장 특성 세트(정규화)
        Set<String> bmCategories = bmCategoryNames(businessMan); //업종
        Set<String> bmMoods = bmMoodNames(businessMan); //분위기
        Set<String> bmPromos = bmPromotionNames(businessMan); //홍보방식

        //혹여나 카테고리가 비어있다라고한다면 쿼리를 굳이 날릴 필요가 없다.
        if(bmCategories.isEmpty()){
            return Collections.emptyList();
        }
        // 3) 후보 인플루언서
        //필터링 후 넘기는건 나중에 고려합시다. 일단 시간이 없어요.
        List<Influencer> influencerList = influencerMatchRepository.findInfluencerByCategoryNames(bmCategories);
        if (influencerList.isEmpty()) return Collections.emptyList();

        // ---------- (A) 전역 파라미터: 베이지안/로그 ----------
        // m: 리뷰 중앙값 기반 의사 리뷰수, [mClampMin, mClampMax]로 클램프
        List<Long> reviewCounts = influencerList
                .stream().map(this::reviewCountOf).toList();
        long medianRc = StatsUtils.median(reviewCounts);
        double m = StatsUtils.clamp((medianRc <= 0 ? (long) props.getMFallback() : medianRc),
                props.getMClampMin(), props.getMClampMax());

        // C: 전역 사전 평균(0~1) = 리뷰 많을수록 실제 전체 평균으로 수렴
        long sumReviews = 0L, sumScores = 0L; // totalScore는 5점 만점 "합계" 가정
        for (Influencer i : influencerList) {
            long rc = reviewCountOf(i);
            Long ts = totalScoreOf(i);
            if (rc > 0 && ts != null) {
                sumReviews += rc;
                sumScores += ts;
            }
        }
        double C = computeGlobalPriorMean01(sumReviews, sumScores);

        //follower_ref: 팔로워 기준(95퍼센타일) — 로그 정규화 분모
        List<Long> followers = influencerList.stream()
                .map(this::followerOf)
                .collect(Collectors.toList());
        long follower_ref = Math.max(1L, StatsUtils.percentile(followers, props.getReachPercentile()));

        // ---------- (B) 후보별 점수 계산 ----------
        record Scored(Influencer influencer, double score01) {
        }
        List<Scored> scored = new ArrayList<>();

        for (Influencer influencer : influencerList) {
            Activity a = influencer.getActivity();
            if (a == null) continue; // 활동 정보 없으면 스킵

            // 인플루언서 특성 세트(정규화)
            Set<String> platformSet = SimilarityUtils.toNormalizedSet(
                    Optional.ofNullable(a.getActivityPlatformList()).orElseGet(List::of).stream()
                            .map(ap -> {
                                ActivityPlatform x = ap;
                                return x.getPlatform() != null ? x.getPlatform().getName() : null;
                            })
                            .filter(Objects::nonNull).toList());

            Set<String> topicSet = SimilarityUtils.toNormalizedSet(
                    concat(
                            Optional.ofNullable(a.getActivityContentTopicList()).orElseGet(List::of).stream()
                                    .map(act -> {
                                        ActivityContentTopic x = act;
                                        return x.getContentTopic() != null ? x.getContentTopic().getName() : null;
                                    }).filter(Objects::nonNull).toList(),
                            Optional.ofNullable(a.getActivityPreferTopicList()).orElseGet(List::of).stream()
                                    .map(apt -> {
                                        ActivityPreferTopic x = apt;
                                        return x.getPreferTopic() != null ? x.getPreferTopic().getName() : null;
                                    }).filter(Objects::nonNull).toList()
                    )
            );

            Set<String> styleSet = SimilarityUtils.toNormalizedSet(
                    Optional.ofNullable(a.getActivityContentStyleList()).orElseGet(List::of).stream()
                            .map(acs -> {
                                ActivityContentStyle x = acs;
                                return x.getContentStyle() != null ? x.getContentStyle().getName() : null;
                            })
                            .filter(Objects::nonNull).toList()
            );

            // (1) 업종↔토픽: 자카드 (정확 매칭)
            double sCategory = SimilarityUtils.jaccard(bmCategories, topicSet);

            // (2) 무드↔스타일: 소프트 자카드 (의미 유사)
            double sMoodStyle = DomainSimilarity.softJaccard(bmMoods, styleSet, DomainSimilarity::moodStyleSim);

            // (3) 홍보↔플랫폼: 소프트 자카드 (의미 유사)
            double sPromoPlatform = DomainSimilarity.softJaccard(bmPromos, platformSet, DomainSimilarity::promoPlatformSim);

            // (4) 평판: 베이지안 보정 (R,C∈[0,1])
            double avg01 = avgRating01Of(influencer); // 리뷰 없으면 0.5(중립)
            double sReputation = SimilarityUtils.bayesianAverage(avg01, reviewCountOf(influencer), C, m);

            // (5) 도달력: 로그 스케일
            double sReach = SimilarityUtils.logScale(followerOf(influencer), follower_ref);

            // (6) 최종(0~1): 가중 합산
            double final01 =
                    props.getWCategory() * sCategory
                            + props.getWMoodStyle() * sMoodStyle
                            + props.getWPromoPlatform() * sPromoPlatform
                            + props.getWReputation() * sReputation
                            + props.getWReach() * sReach;

            scored.add(new Scored(influencer, final01));

        }
        // ---------- (C) 정렬 → TOP5 → 프론트 DTO 매핑 ----------
        return scored.stream()
                .sorted(Comparator.comparingDouble(Scored::score01).reversed())
                .limit(5)
                .map(s -> toRes(s.influencer(), s.score01))
                .toList();


    }
    private RecommendInfluencerRes toRes (Influencer influencer, double score01) {

        User user = influencer.getUser();
        Activity activity = influencer.getActivity();

        List<String> platformList = activity.getActivityPlatformList().stream()
                .map(ap -> ap.getPlatform().getName())
                .toList();

        List<String> contentTopicList = activity.getActivityContentTopicList().stream()
                .map(act -> act.getContentTopic().getName())
                .toList();

        double weightSum = props.getWCategory() + props.getWMoodStyle() + props.getWPromoPlatform()
                + props.getWReputation() + props.getWReach();

        //가중치의 합이 몇인지에 상관없이 100점만점의 환경으로 변환하는 로직
        int finalScore = 0;
        if(weightSum > 0){
            finalScore = (int)Math.round((score01/weightSum) * 100);
        }
        return new RecommendInfluencerRes(
                user.getProfileImage(),
                user.getNickname(),
                formatAvgScore(influencer.getTotalScore(), influencer.getReviewCount()),
                influencer.getReviewCount(),
                platformList,
                activity.getFollowerCount(),
                activity.getMinAmount(),
                contentTopicList,
                finalScore
        );
    }


    // ================== 내부 헬퍼/공용 ==================

    /* 전역 사전평균 C(0~1): C=(N/(N+mC))*globalAvg01+(mC/(N+mC))*C0 → [cClampMin,cClampMax] */
    private double computeGlobalPriorMean01(long sumReviews, long sumScores) {
        double C0 = props.getC0();
        double mC = props.getMC();
        double C = C0;
        if (sumReviews > 0) {
            double globalAvg01 = ((double) sumScores / (double) sumReviews) / 5.0; // 5점 만점 가정
            double N = (double) sumReviews;
            C = (N / (N + mC)) * globalAvg01 + (mC / (N + mC)) * C0;
        }
        return StatsUtils.clamp(C, props.getCClampMin(), props.getCClampMax());
    }

    /**
     * 평균 평점(0~1) — 리뷰 없거나 합계 없으면 0.5(중립)
     */
    private double avgRating01Of(Influencer influencer) {
        long rc = reviewCountOf(influencer);
        Long ts = totalScoreOf(influencer);
        if (rc <= 0 || ts == null) return 0.5;
        double avg5 = ((double) ts) / rc;
        double v = avg5 / 5.0;
        return Math.max(0.0, Math.min(1.0, v));
    }

    /**
     * 평균 평점 문자열(소수 2자리). 리뷰 0이면 "0.00"
     */
    private String formatAvgScore(Long totalScore, long reviewCount) {
        if (reviewCount <= 0 || totalScore == null) return "0.00";
        double avg5 = ((double) totalScore) / reviewCount; // 5점 만점 평균
        return new DecimalFormat("0.00").format(avg5);
    }

    /**
     * 리뷰 수 — null 방어
     */
    private long reviewCountOf(Influencer influencer) {
        Long v = influencer.getReviewCount();
        return (v == null) ? 0L : Math.max(0L, v);
    }

    /**
     * 합계 평점(5점 만점 합계) — 없으면 null
     */
    private Long totalScoreOf(Influencer influencer) {
        return influencer.getTotalScore();
    }

    /**
     * 팔로워 — Activity에 없으면 0
     */
    private long followerOf(Influencer influencer) {
        Activity a = influencer.getActivity();
        if (a == null || a.getFollowerCount() == null) return 0L;
        return Math.max(0L, a.getFollowerCount());
    }


    /**
     * 리스트 결합(Null-safe)
     */
    private static List<String> concat(List<String> a, List<String> b) {
        List<String> r = new ArrayList<>();
        if (a != null) r.addAll(a);
        if (b != null) r.addAll(b);
        return r;
    }

    /**
     * 중복 제거 + 입력 순서 보존 (UI 표시에 유리)
     */
    private static List<String> dedupPreserveOrder(List<String> list) {
        if (list == null) return Collections.emptyList();
        LinkedHashSet<String> s = new LinkedHashSet<>();
        for (String x : list) if (x != null && !x.isBlank()) s.add(x);
        return new ArrayList<>(s);
    }

    private Set<String> bmCategoryNames(BusinessMan bm) {
        WorkPlace wp = bm.getWorkPlace();
        if (wp == null) return Collections.emptySet();
        List<WorkPlaceCategory> list = Optional.ofNullable(wp.getCategoryList()).orElseGet(List::of);
        List<String> names = new ArrayList<>();
        for (WorkPlaceCategory wpc : list) {
            if (wpc.getCategory() != null && wpc.getCategory().getName() != null)
                names.add(wpc.getCategory().getName());
        }
        return SimilarityUtils.toNormalizedSet(names);
    }

    private Set<String> bmMoodNames(BusinessMan bm) {
        WorkPlace wp = bm.getWorkPlace();
        if (wp == null) return Collections.emptySet();
        List<WorkPlaceMood> list = Optional.ofNullable(wp.getMoodList()).orElseGet(List::of);
        List<String> names = new ArrayList<>();
        for (WorkPlaceMood wpm : list) {
            if (wpm.getMood() != null && wpm.getMood().getName() != null) names.add(wpm.getMood().getName());
        }
        return SimilarityUtils.toNormalizedSet(names);
    }

    private Set<String> bmPromotionNames(BusinessMan bm) {
        WorkPlace wp = bm.getWorkPlace();
        if (wp == null) return Collections.emptySet();
        List<WorkPlacePromotion> list = Optional.ofNullable(wp.getPromotionList()).orElseGet(List::of);
        List<String> names = new ArrayList<>();
        for (WorkPlacePromotion wpp : list) {
            if (wpp.getPromotion() != null && wpp.getPromotion().getName() != null)
                names.add(wpp.getPromotion().getName());
        }
        return SimilarityUtils.toNormalizedSet(names);
    }
}
