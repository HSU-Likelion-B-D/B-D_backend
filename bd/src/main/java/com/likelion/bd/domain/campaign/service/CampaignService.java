package com.likelion.bd.domain.campaign.service;

import com.likelion.bd.domain.campaign.entity.CampaignStatus;
import com.likelion.bd.domain.campaign.web.dto.CampaignCreateReq;
import com.likelion.bd.domain.campaign.web.dto.CampaignListRes;
import com.likelion.bd.domain.campaign.web.dto.CampaignResponseReq;
import com.likelion.bd.global.jwt.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CampaignService {

    void createCampaign(CampaignCreateReq campaignCreateReq, UserPrincipal userPrincipal);

    Page<CampaignListRes> showCampaign(UserPrincipal userPrincipal, CampaignStatus status,Boolean all, Pageable pageable);

    void updateCampaign(CampaignResponseReq campaignResponseReq, UserPrincipal userPrincipal);
}
