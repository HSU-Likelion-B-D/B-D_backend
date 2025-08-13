package com.likelion.bd.domain.influencer.service;

import com.likelion.bd.domain.influencer.web.dto.ActivityCreateReq;
import com.likelion.bd.domain.influencer.web.dto.ActivityCreateRes;
import com.likelion.bd.domain.influencer.web.dto.InfluencerMyPageRes;

public interface InfluencerService {

    ActivityCreateRes createActivity(ActivityCreateReq activityCreateReq);

    InfluencerMyPageRes myPage(Long userId);
}
