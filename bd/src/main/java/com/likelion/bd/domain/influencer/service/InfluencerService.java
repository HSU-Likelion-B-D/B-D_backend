package com.likelion.bd.domain.influencer.service;

import com.likelion.bd.domain.influencer.web.dto.ActivityCreateReq;
import com.likelion.bd.domain.influencer.web.dto.ActivityCreateRes;

public interface InfluencerService {

    ActivityCreateRes createActivity(ActivityCreateReq activityCreateReq, Long userId);
}
