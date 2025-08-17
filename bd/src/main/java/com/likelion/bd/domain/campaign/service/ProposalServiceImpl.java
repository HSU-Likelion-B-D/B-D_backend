package com.likelion.bd.domain.campaign.service;

import com.likelion.bd.domain.campaign.entity.Proposal;
import com.likelion.bd.domain.campaign.repository.ProposalRepository;
import com.likelion.bd.domain.campaign.web.dto.ProposalWriteReq;
import com.likelion.bd.domain.campaign.web.dto.ProposalWriteRes;
import com.likelion.bd.domain.user.entity.UserRoleType;
import com.likelion.bd.global.jwt.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProposalServiceImpl implements ProposalService {

    private final ProposalRepository proposalRepository;

    @Override
    @Transactional
    // 제안서 작성/수정
    public ProposalWriteRes writeProposal(ProposalWriteReq proposalWriteReq, UserPrincipal userPrincipal) {

        Optional<Proposal> optionalProposal = proposalRepository.findByWriterId(userPrincipal.getId());

        Proposal proposal = optionalProposal
                .map(existProposal -> {
                    existProposal.updateProposal(proposalWriteReq, userPrincipal.getRole());
                    return existProposal;
                })
                .orElseGet(() -> {
                    LocalDate startDate = LocalDate.parse(proposalWriteReq.getStartDate());
                    LocalDate endDate = LocalDate.parse(proposalWriteReq.getEndDate());

                    String contentTopic = null; // 자영업자면 그냥 null
                    if (UserRoleType.valueOf(userPrincipal.getRole()) == UserRoleType.INFLUENCER) {
                        if (proposalWriteReq.getContentTopic() != null && !proposalWriteReq.getContentTopic().isEmpty()) {
                            contentTopic = proposalWriteReq.getContentTopic();
                        }
                    }

                    Proposal saveProposal = Proposal.builder()
                            .writerId(userPrincipal.getId())
                            .writeRole(UserRoleType.valueOf(userPrincipal.getRole()))
                            .title(proposalWriteReq.getTitle())
                            .offerAmount(proposalWriteReq.getOfferAmount())
                            .startDate(startDate)
                            .endDate(endDate)
                            .overView(proposalWriteReq.getOverView())
                            .request(proposalWriteReq.getRequest())
                            .contentTopic(contentTopic) // 인플루언서 전용 필드
                            .build();

                    return proposalRepository.save(saveProposal);
                });

        return new ProposalWriteRes(proposal.getProposalId());
    }
}
