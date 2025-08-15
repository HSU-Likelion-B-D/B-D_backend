package com.likelion.bd.domain.influencer.service;

import com.likelion.bd.domain.influencer.web.dto.ActivityCreateReq;
import com.likelion.bd.domain.influencer.web.dto.ActivityCreateRes;
import com.likelion.bd.domain.influencer.web.dto.ActivityUpdateReq;
import com.likelion.bd.domain.influencer.web.dto.InfluencerMyPageRes;

public interface InfluencerService {

    ActivityCreateRes createActivity(ActivityCreateReq activityCreateReq);

    void updateActivity(ActivityUpdateReq activityUpdateReq, Long userId);

    InfluencerMyPageRes myPage(Long userId);
}
