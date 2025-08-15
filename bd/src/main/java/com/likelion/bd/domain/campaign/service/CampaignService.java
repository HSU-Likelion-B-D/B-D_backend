package com.likelion.bd.domain.campaign.service;

import com.likelion.bd.domain.campaign.entity.CampaignStatus;
import com.likelion.bd.domain.campaign.web.dto.CampaignListRes;
import com.likelion.bd.global.jwt.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CampaignService {
    Page<CampaignListRes> showCampaign(UserPrincipal userPrincipal, CampaignStatus status,Boolean all, Pageable pageable);
}
