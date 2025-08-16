package com.likelion.bd.domain.campaign.service;

import com.likelion.bd.domain.campaign.entity.Proposal;
import com.likelion.bd.domain.campaign.repository.ProposalRepository;
import com.likelion.bd.domain.campaign.web.dto.ProposalWriteReq;
import com.likelion.bd.domain.campaign.web.dto.ProposalWriteRes;
import com.likelion.bd.domain.user.entity.UserRoleType;
import com.likelion.bd.global.exception.CustomException;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.code.campaign.ProposalErrorResponseCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ProposalServiceImpl implements ProposalService {

    private final ProposalRepository proposalRepository;

    @Override
    @Transactional
    public ProposalWriteRes writeProposal(ProposalWriteReq proposalWriteReq, UserPrincipal userPrincipal) {
        proposalRepository.findByWriterId(userPrincipal.getId())
                .ifPresent(proposal -> {
                    // 존재할 경우, 예외를 발생시켜 중복 생성을 막습니다.
                    throw new CustomException(ProposalErrorResponseCode.PROPOSAL_ALREADY_EXISTS_409); // 예시 에러
                });

        LocalDate startDate = LocalDate.parse(proposalWriteReq.getStartDate());
        LocalDate endDate = LocalDate.parse(proposalWriteReq.getEndDate());
        System.out.println(UserRoleType.valueOf(userPrincipal.getRole()));

        Proposal proposal = Proposal.builder()
                .writerId(userPrincipal.getId())
                .writeRole(UserRoleType.valueOf(userPrincipal.getRole()))
                .title(proposalWriteReq.getTitle())
                .offerAmount(proposalWriteReq.getOfferAmount())
                .startDate(startDate)
                .endDate(endDate)
                .overView(proposalWriteReq.getOverView())
                .request(proposalWriteReq.getRequest())
                .build();

        Proposal saveProposal = proposalRepository.save(proposal);

        return new ProposalWriteRes(
                saveProposal.getProposalId()
        );
    }
}
