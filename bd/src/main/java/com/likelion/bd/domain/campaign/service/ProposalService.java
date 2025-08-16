package com.likelion.bd.domain.campaign.service;

import com.likelion.bd.domain.campaign.web.dto.ProposalWriteReq;
import com.likelion.bd.domain.campaign.web.dto.ProposalWriteRes;
import com.likelion.bd.global.jwt.UserPrincipal;

public interface ProposalService {

    ProposalWriteRes writeProposal(ProposalWriteReq proposalWriteReq, UserPrincipal userPrincipal); // 제안서 작성
}
