package com.likelion.bd.domain.influencer.service;

import com.likelion.bd.domain.influencer.entity.*;
import com.likelion.bd.domain.influencer.repository.*;
import com.likelion.bd.domain.influencer.web.dto.ActivityCreateReq;
import com.likelion.bd.domain.influencer.web.dto.ActivityCreateRes;
import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.domain.user.repository.UserRepository;
import com.likelion.bd.global.exception.CustomException;
import com.likelion.bd.global.response.code.Influencer.ActivityErrorCode;
import com.likelion.bd.global.response.code.user.UserErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public ActivityCreateRes createActivity(
            ActivityCreateReq activityCreateReq,
            Long userId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        // -------------------------------------------------------------------------------------------------------

        Activity activity = Activity.builder() // 유효성 검사를 다 통과하고 넘어왔다는 가정
                .activityName(activityCreateReq.getActivityName())
                .snsUrl(activityCreateReq.getSnsUrl())
                .followerCountRange(FollowerCountRange.fromValue(activityCreateReq.getFollowerCountRange()))
                .uploadFrequency(UploadFrequency.fromValue(activityCreateReq.getUploadFrequency()))
                .bankName(activityCreateReq.getBankName())
                .accountNumber(activityCreateReq.getAccountNumber())
                .minAmount(activityCreateReq.getMinAmount())
                .maxAmount(activityCreateReq.getMaxAmount())
                .build();
        activityRepository.save(activity);

        // -------------------------------------------------------------------------------------------------------

        for (Long platformId : activityCreateReq.getPlatformIds()) {
            Platform platform = platformRepository.findById(platformId)
                    .orElseThrow(() -> new CustomException(ActivityErrorCode.PLATFORM_NOT_FOUND_404));

            ActivityPlatform ap = ActivityPlatform.builder()
                    .activity(activity)
                    .platform(platform)
                    .build();

            activity.addActivityPlatform(ap);
        }

        for (Long contentTopicId : activityCreateReq.getContentTopicIds()) {
            ContentTopic contentTopic = contentTopicRepository.findById(contentTopicId)
                    .orElseThrow(() -> new CustomException(ActivityErrorCode.CONTENTTOPIC_NOT_FOUND_404));

            ActivityContentTopic act = ActivityContentTopic.builder()
                    .activity(activity)
                    .contentTopic(contentTopic)
                    .build();

            activity.addActivityContentTopic(act);
        }

        for (Long contentStyleId : activityCreateReq.getContentStyleIds()) {
            ContentStyle contentStyle = contentStyleRepository.findById(contentStyleId)
                    .orElseThrow(() -> new CustomException(ActivityErrorCode.CONTENTSTYLE_NOT_FOUND_404));

            ActivityContentStyle acs = ActivityContentStyle.builder()
                    .activity(activity)
                    .contentStyle(contentStyle)
                    .build();

            activity.addActivityContentStyle(acs);
        }

        for (Long preferTopicId : activityCreateReq.getPreferTopicIds()) {
            PreferTopic preferTopic = preferTopicRepository.findById(preferTopicId)
                    .orElseThrow(() -> new CustomException(ActivityErrorCode.PREPERTOPIC_NOT_FOUND_404));

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
                .build();
        influencerRepository.save(influencer);

        // -------------------------------------------------------------------------------------------------------

        return new ActivityCreateRes(
                influencer.getInfluencerId()
        );
    }
}
