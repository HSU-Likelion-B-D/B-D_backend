package com.likelion.bd.domain.influencer.service;

import com.likelion.bd.domain.businessman.entity.BusinessMan;
import com.likelion.bd.domain.businessman.repository.BusinessManRepository;
import com.likelion.bd.domain.influencer.entity.*;
import com.likelion.bd.domain.influencer.repository.*;
import com.likelion.bd.domain.influencer.web.dto.*;
import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.domain.user.repository.UserRepository;
import com.likelion.bd.global.exception.CustomException;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.code.Influencer.ActivityErrorResponseCode;
import com.likelion.bd.global.response.code.Influencer.InfluencerErrorResponseCode;
import com.likelion.bd.global.response.code.user.UserErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InfluencerServiceImpl implements InfluencerService {

    private final UserRepository userRepository;
    private final InfluencerRepository influencerRepository;
    private final ActivityRepository activityRepository;
    private final PlatformRepository platformRepository;
    private final ContentTopicRepository contentTopicRepository;
    private final ContentStyleRepository contentStyleRepository;
    private final PreferTopicRepository preferTopicRepository;
    private final BusinessManRepository businessManRepository;

    @Override
    @Transactional
    public ActivityCreateRes createActivity(
            ActivityCreateReq activityCreateReq
    ) {
        User user = userRepository.findById(activityCreateReq.getUserId())
                .orElseThrow(() -> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        influencerRepository.findByUserUserId(activityCreateReq.getUserId())
                .ifPresent(influencer -> {
                    throw new CustomException(InfluencerErrorResponseCode.INFLUENCER_DUPLICATE_409); // 예시 에러
                });

        // -------------------------------------------------------------------------------------------------------

        Activity activity = Activity.builder() // 유효성 검사를 다 통과하고 넘어왔다는 가정
                .activityName(activityCreateReq.getActivityName())
                .snsUrl(activityCreateReq.getSnsUrl())
                .followerCount(activityCreateReq.getFollowerCount())
                .uploadFrequency(UploadFrequency.fromValue(activityCreateReq.getUploadFrequency()))
                .bankName(activityCreateReq.getBankName())
                .accountNumber(activityCreateReq.getAccountNumber())
                .minBudget(activityCreateReq.getMinBudget())
                .maxBudget(activityCreateReq.getMaxBudget())
                .build();
        activityRepository.save(activity);

        // -------------------------------------------------------------------------------------------------------

        for (Long platformId : activityCreateReq.getPlatformIds()) {
            Platform platform = platformRepository.findById(platformId)
                    .orElseThrow(() -> new CustomException(ActivityErrorResponseCode.PLATFORM_NOT_FOUND_404));

            ActivityPlatform ap = ActivityPlatform.builder()
                    .activity(activity)
                    .platform(platform)
                    .build();

            activity.addActivityPlatform(ap);
        }

        for (Long contentTopicId : activityCreateReq.getContentTopicIds()) {
            ContentTopic contentTopic = contentTopicRepository.findById(contentTopicId)
                    .orElseThrow(() -> new CustomException(ActivityErrorResponseCode.CONTENTTOPIC_NOT_FOUND_404));

            ActivityContentTopic act = ActivityContentTopic.builder()
                    .activity(activity)
                    .contentTopic(contentTopic)
                    .build();

            activity.addActivityContentTopic(act);
        }

        for (Long contentStyleId : activityCreateReq.getContentStyleIds()) {
            ContentStyle contentStyle = contentStyleRepository.findById(contentStyleId)
                    .orElseThrow(() -> new CustomException(ActivityErrorResponseCode.CONTENTSTYLE_NOT_FOUND_404));

            ActivityContentStyle acs = ActivityContentStyle.builder()
                    .activity(activity)
                    .contentStyle(contentStyle)
                    .build();

            activity.addActivityContentStyle(acs);
        }

        for (Long preferTopicId : activityCreateReq.getPreferTopicIds()) {
            PreferTopic preferTopic = preferTopicRepository.findById(preferTopicId)
                    .orElseThrow(() -> new CustomException(ActivityErrorResponseCode.PREPERTOPIC_NOT_FOUND_404));

            ActivityPreferTopic apt = ActivityPreferTopic.builder()
                    .activity(activity)
                    .preferTopic(preferTopic)
                    .build();

            activity.addActivityPreferTopic(apt);
        }

        activityRepository.save(activity);
        // -------------------------------------------------------------------------------------------------------

        Influencer influencer = Influencer.builder()
                .user(user)
                .activity(activity)
                .totalScore(0.0)
                .reviewCount(0L)
                .build();
        influencerRepository.save(influencer);

        // -------------------------------------------------------------------------------------------------------

        return new ActivityCreateRes(
                influencer.getInfluencerId()
        );
    }

    @Override
    public ActivityFormRes getActcivity(UserPrincipal userPrincipal) {
        Influencer influencer = influencerRepository.findByUserUserId(userPrincipal.getId())
                .orElseThrow(() -> new CustomException(InfluencerErrorResponseCode.INFLUENCER_NOT_FOUND_404));

        Activity activity = influencer.getActivity();

        List<String> platforms = activity.getPlatforms();
        List<String> contentTopics = activity.getContentTopics();
        List<String> contentStyles = activity.getContentStyles();
        List<String> preferTopics = activity.getPreferTopics();

        return new ActivityFormRes(
                influencer.getInfluencerId(),
                activity.getActivityName(),
                activity.getSnsUrl(),
                activity.getFollowerCount(),
                activity.getUploadFrequency().getValue(),
                activity.getBankName(),
                activity.getAccountNumber(),
                activity.getMinBudget(),
                activity.getMaxBudget(),
                platforms,
                contentTopics,
                contentStyles,
                preferTopics
        );
    }

    @Override
    @Transactional
    public void updateActivity(
            ActivityUpdateReq activityUpdateReq,
            Long userId
    ) {

        Influencer influencer = influencerRepository.findByUserUserId(userId)
                .orElseThrow(() -> new CustomException(InfluencerErrorResponseCode.INFLUENCER_NOT_FOUND_404));

        Activity activity = activityRepository.findById(influencer.getActivity().getActivityId())
                .orElseThrow(() -> new CustomException(ActivityErrorResponseCode.ACTIVITY_NOT_FOUND_404));

        String activityName = activity.getActivityName();
        String snsUrl = activity.getSnsUrl();
        Long followerCount = activity.getFollowerCount();
        UploadFrequency uploadFrequency = activity.getUploadFrequency();
        String bankName = activity.getBankName();
        String accountNumber = activity.getAccountNumber();
        String minBudget = activity.getMinBudget();
        String maxBudget = activity.getMaxBudget();

        if (activityUpdateReq.getActivityName() != null && !activityUpdateReq.getActivityName().isEmpty()) {
            activityName = activityUpdateReq.getActivityName();
        }
        if (activityUpdateReq.getSnsUrl() != null && !activityUpdateReq.getSnsUrl().isEmpty()) {
            snsUrl = activityUpdateReq.getSnsUrl();
        }
        if (activityUpdateReq.getFollowerCount() != null) {
            followerCount = activityUpdateReq.getFollowerCount();
        }
        if (activityUpdateReq.getUploadFrequency() != null) {
            uploadFrequency = UploadFrequency.fromValue(activityUpdateReq.getUploadFrequency());
        }
        if (activityUpdateReq.getBankName() != null && !activityUpdateReq.getBankName().isEmpty()) {
            bankName = activityUpdateReq.getBankName();
        }
        if  (activityUpdateReq.getAccountNumber() != null && !activityUpdateReq.getAccountNumber().isEmpty()) {
            accountNumber = activityUpdateReq.getAccountNumber();
        }
        if (activityUpdateReq.getMinBudget() != null) {
            minBudget = activityUpdateReq.getMinBudget();
        }
        if (activityUpdateReq.getMaxBudget() != null) {
            maxBudget = activityUpdateReq.getMaxBudget();
        }

        activity.updateBasicInfo(
                activityName, snsUrl, followerCount, uploadFrequency,
                bankName, accountNumber, minBudget, maxBudget);

        // -------------------------------------------------------------------------------------------------------

        if (activityUpdateReq.getPlatformIds() != null && !activityUpdateReq.getPlatformIds().isEmpty()) {
            activity.getActivityPlatformList().clear();

            for (Long platformId : activityUpdateReq.getPlatformIds()) {
                Platform platform = platformRepository.findById(platformId)
                        .orElseThrow(() -> new CustomException(ActivityErrorResponseCode.PLATFORM_NOT_FOUND_404));

                ActivityPlatform ap = ActivityPlatform.builder()
                        .activity(activity)
                        .platform(platform)
                        .build();

                activity.addActivityPlatform(ap);
            }
        }

        if (activityUpdateReq.getContentTopicIds() != null && !activityUpdateReq.getContentTopicIds().isEmpty()) {
            activity.getActivityContentTopicList().clear();

            for (Long contentTopicId : activityUpdateReq.getContentTopicIds()) {
                ContentTopic contentTopic = contentTopicRepository.findById(contentTopicId)
                        .orElseThrow(() -> new CustomException(ActivityErrorResponseCode.CONTENTTOPIC_NOT_FOUND_404));

                ActivityContentTopic act = ActivityContentTopic.builder()
                        .activity(activity)
                        .contentTopic(contentTopic)
                        .build();

                activity.addActivityContentTopic(act);
            }
        }

        if (activityUpdateReq.getContentStyleIds() != null && !activityUpdateReq.getContentStyleIds().isEmpty()) {
            activity.getActivityContentStyleList().clear();

            for (Long contentStyleId : activityUpdateReq.getContentStyleIds()) {
                ContentStyle contentStyle = contentStyleRepository.findById(contentStyleId)
                        .orElseThrow(() -> new CustomException(ActivityErrorResponseCode.CONTENTSTYLE_NOT_FOUND_404));

                ActivityContentStyle acs = ActivityContentStyle.builder()
                        .activity(activity)
                        .contentStyle(contentStyle)
                        .build();

                activity.addActivityContentStyle(acs);
            }
        }

        if (activityUpdateReq.getPreferTopicIds() != null && !activityUpdateReq.getPreferTopicIds().isEmpty()) {
            activity.getActivityPreferTopicList().clear();

            for (Long preferTopicId : activityUpdateReq.getPreferTopicIds()) {
                PreferTopic preferTopic = preferTopicRepository.findById(preferTopicId)
                        .orElseThrow(() -> new CustomException(ActivityErrorResponseCode.PREPERTOPIC_NOT_FOUND_404));

                ActivityPreferTopic apt = ActivityPreferTopic.builder()
                        .activity(activity)
                        .preferTopic(preferTopic)
                        .build();

                activity.addActivityPreferTopic(apt);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public InfluencerMyPageRes myPage(Long userId) {

        Influencer influencer = influencerRepository.findByUserUserId(userId)
                .orElseThrow(() -> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        User user = influencer.getUser();
        Activity activity = influencer.getActivity();

        List<String> platforms = activity.getPlatforms();
        List<String> contentTopics = activity.getContentTopics();
        List<String> contentStyles = activity.getContentStyles();
        List<String> preferTopics = activity.getPreferTopics();

        // 382K+, 3M+ ...
        String formattedFollowers = activity.formatFollowers();

        // 소수점 2자리까지 처리
        BigDecimal avgScore = BigDecimal.ZERO;
        if (influencer.getReviewCount() > 0) {
            avgScore = BigDecimal.valueOf(influencer.getTotalScore())
                    .divide(BigDecimal.valueOf(influencer.getReviewCount()), 2, RoundingMode.HALF_UP);
        }
        String avgText = String.format("%.2f", avgScore);

        return new InfluencerMyPageRes(
                user.getProfileImage(),
                user.getNickname(),
                user.getIntroduction(),
                activity.getActivityName(),
                formattedFollowers,
                avgText,
                influencer.getReviewCount(),
                activity.getSnsUrl(),
                activity.getMinBudget(),
                platforms,
                contentTopics,
                contentStyles,
                preferTopics
        );
    }

    @Override
    public InfluencerHomeRes home(Long userId) {
        Influencer influencer = influencerRepository.findByUserUserId(userId)
                .orElseThrow(() -> new CustomException(InfluencerErrorResponseCode.INFLUENCER_NOT_FOUND_404));

        User user = influencer.getUser();
        Activity activity = influencer.getActivity();
        String avgText = influencer.formatAverageScore(influencer.getTotalScore(), influencer.getReviewCount());

        Pageable topFour = PageRequest.of(0, 4);
        List<BusinessMan> topbusinessMans = businessManRepository.findTopBusinessManAverageScoreDesc(topFour);
        List<InfluencerHomeRes.BusinessManSummaryRes> businessMans = topbusinessMans.stream()
                .map(businessMan -> new InfluencerHomeRes.BusinessManSummaryRes(
                        businessMan.getUser().getUserId(),
                        businessMan.getUser().getNickname(),
                        businessMan.getUser().getProfileImage(),
                        businessMan.formatAverageScore(businessMan.getTotalScore(), businessMan.getReviewCount()),
                        businessMan.getReviewCount()
                ))
                .toList();

        return new InfluencerHomeRes(
                user.getProfileImage(),
                user.getNickname(),
                activity.getActivityName(),
                avgText,
                influencer.getReviewCount(),
                businessMans
        );
    }

    @Override
    public InfluencerBankInfoRes bankInfo(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        Influencer influencer = influencerRepository.findByUserUserId(userId)
                .orElseThrow(() -> new CustomException(InfluencerErrorResponseCode.INFLUENCER_NOT_FOUND_404));

        return new InfluencerBankInfoRes(
                user.getName(),
                influencer.getActivity().getBankName(),
                influencer.getActivity().getAccountNumber()
        );
    }
}
