package com.likelion.bd.domain.influencer.service;

import com.likelion.bd.domain.influencer.web.dto.*;
import com.likelion.bd.global.jwt.UserPrincipal;

public interface InfluencerService {

    ActivityCreateRes createActivity(ActivityCreateReq activityCreateReq);

    ActivityFormRes getActcivity(UserPrincipal userPrincipal);

    void updateActivity(ActivityUpdateReq activityUpdateReq, Long userId);

    InfluencerMyPageRes myPage(Long userId);

    InfluencerHomeRes home(Long userId);

    InfluencerBankInfoRes bankInfo(Long userId);
}
