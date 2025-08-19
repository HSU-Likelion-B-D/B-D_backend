package com.likelion.bd.domain.match.service;

import com.likelion.bd.domain.businessman.entity.*;
import com.likelion.bd.domain.influencer.entity.*;
import com.likelion.bd.domain.match.repository.BusinessManMatchRepository;
import com.likelion.bd.domain.match.repository.InfluencerMatchRepository;
import com.likelion.bd.domain.match.web.dto.RecommendInfluencerRes;
import com.likelion.bd.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

// 실제 MatchProperties를 로드하기 위해 @SpringBootTest로 변경
@SpringBootTest
class MatchServiceImplTest {

    @Autowired // 실제 설정이 주입된 서비스 사용
    private MatchServiceImpl matchService;

    @MockBean // SpringBootTest에서 Mock 객체를 사용하기 위한 어노테이션
    private BusinessManMatchRepository businessManMatchRepository;

    @MockBean
    private InfluencerMatchRepository influencerMatchRepository;

    private BusinessMan testBusinessMan;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 생성
        testBusinessMan = createTestBusinessMan();
    }

    @Test
    @DisplayName("[시나리오 1] 사전에 정의된 데이터로 순위 예측 테스트")
    void top5ForBusinessMan_PredictableTest() {
        // given
        List<Influencer> predictableInfluencers = createPredictableInfluencers();
        when(businessManMatchRepository.findById(1L)).thenReturn(Optional.of(testBusinessMan));
        when(influencerMatchRepository.findInfluencerByCategoryNames(anySet())).thenReturn(predictableInfluencers);

        // when
        List<RecommendInfluencerRes> result = matchService.top5ForBusinessMan(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(5);

        System.out.println("\n--- [시나리오 1] 순위 예측 테스트 결과 ---");
        result.forEach(res -> System.out.println(res.toString()));
    }

    @Test
    @DisplayName("[시나리오 2] 실제 상황을 가정한 데이터로 알고리즘 성향 테스트")
    void top5ForBusinessMan_RealWorldTest() {
        // given
        List<Influencer> realisticInfluencers = createRealisticInfluencers();
        when(businessManMatchRepository.findById(1L)).thenReturn(Optional.of(testBusinessMan));
        when(influencerMatchRepository.findInfluencerByCategoryNames(anySet())).thenReturn(realisticInfluencers);

        // when
        List<RecommendInfluencerRes> result = matchService.top5ForBusinessMan(1L);

        // then
        assertThat(result).isNotNull();
        // 현실적인 데이터는 4명이므로 4개가 나와야 함
        assertThat(result).hasSize(4);

        System.out.println("\n--- [시나리오 2] 실제 상황 가정 테스트 결과 ---");
        System.out.println("현재 가중치 설정은 어떤 성향의 인플루언서를 선호하는지 확인해보세요.");
        result.forEach(res -> System.out.println(res.toString()));
    }


    // --- 테스트 데이터 생성 헬퍼 메소드 ---

    private BusinessMan createTestBusinessMan() {
        User user = User.builder().userId(1L).name("자영업자").nickname("사장님").build();
        WorkPlace workPlace = WorkPlace.builder()
                .workplaceId(1L).name("대박식당").address("서울")
                .openTime(LocalTime.of(9, 0)).closeTime(LocalTime.of(22, 0))
                .minBudget(10000L).maxBudget(50000L).isOnline(false).build();

        workPlace.getCategoryList().add(new WorkPlaceCategory(workPlace, Category.builder().id(1L).name("한식").build()));
        workPlace.getMoodList().add(new WorkPlaceMood(workPlace, Mood.builder().id(1L).name("활기찬").build()));
        workPlace.getPromotionList().add(new WorkPlacePromotion(workPlace, Promotion.builder().id(1L).name("가성비").build()));

        return BusinessMan.builder().businessManId(1L).user(user).workPlace(workPlace).build();
    }

    private List<Influencer> createPredictableInfluencers() {
        List<Influencer> influencers = new ArrayList<>();
        influencers.add(createInfluencer(1L, "predictable_perfect_match", Set.of("한식", "맛집탐방"), 100000L, 50L, 240L));
        influencers.add(createInfluencer(2L, "predictable_good_match", Set.of("한식"), 50000L, 40L, 180L));
        influencers.add(createInfluencer(3L, "predictable_high_reach", Set.of("양식"), 500000L, 10L, 40L));
        influencers.add(createInfluencer(4L, "predictable_high_rep", Set.of("중식"), 10000L, 100L, 490L));
        influencers.add(createInfluencer(5L, "predictable_mismatch", Set.of("패션"), 20000L, 5L, 20L));
        return influencers;
    }

    private List<Influencer> createRealisticInfluencers() {
        List<Influencer> influencers = new ArrayList<>();
        // A: 니즈 완벽, 인지도 낮음
        influencers.add(createInfluencer(101L, "A_니치전문가", Set.of("한식", "숨은맛집"), 5000L, 200L, 980L)); // 4.9 stars
        // B: 인지도 최상, 전문성 보통
        influencers.add(createInfluencer(102L, "B_메가인플루언서", Set.of("맛집탐방", "일상"), 500000L, 1000L, 4200L)); // 4.2 stars
        // C: 다방면으로 준수함
        influencers.add(createInfluencer(103L, "C_올라운더", Set.of("한식", "강남맛집"), 50000L, 300L, 1350L)); // 4.5 stars
        // D: 잠재력 있는 신인
        influencers.add(createInfluencer(104L, "D_잠재력신인", Set.of("한식", "요리"), 1000L, 10L, 50L)); // 5.0 stars
        return influencers;
    }

    private Influencer createInfluencer(Long id, String nickname, Set<String> topics, Long followerCount, Long reviewCount, Long totalScore) {
        User user = User.builder().userId(id).name("인플루언서" + id).nickname(nickname).profileImage("http://image.com/" + nickname).build();
        Activity activity = Activity.builder()
                .activityId(id).activityName(nickname).snsUrl("http://sns.com/" + nickname)
                .followerCount(followerCount).uploadFrequency(UploadFrequency.ONCE_OR_TWICE)
                .bankName("테스트은행").accountNumber("12345").minAmount(1000L).maxAmount(10000L).build();

        topics.forEach(topicName -> {
            ContentTopic topic = ContentTopic.builder().name(topicName).build();
            activity.getActivityContentTopicList().add(ActivityContentTopic.builder().activity(activity).contentTopic(topic).build());
        });
        // 테스트 간소화를 위해 style, platform은 생략
        return Influencer.builder()
                .InfluencerId(id).user(user).activity(activity)
                .reviewCount(reviewCount).totalScore(totalScore).build();
    }
}