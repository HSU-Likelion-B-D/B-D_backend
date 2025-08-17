package com.likelion.bd.domain.campaign.service;

import com.likelion.bd.domain.businessman.entity.BusinessMan;
import com.likelion.bd.domain.businessman.repository.BusinessManRepository;
import com.likelion.bd.domain.campaign.entity.Proposal;
import com.likelion.bd.domain.campaign.repository.ProposalRepository;
import com.likelion.bd.domain.campaign.web.dto.ProposalFormRes;
import com.likelion.bd.domain.campaign.web.dto.ProposalWriteReq;
import com.likelion.bd.domain.campaign.web.dto.ProposalWriteRes;
import com.likelion.bd.domain.influencer.entity.Influencer;
import com.likelion.bd.domain.influencer.repository.InfluencerRepository;
import com.likelion.bd.domain.user.entity.UserRoleType;
import com.likelion.bd.global.exception.CustomException;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.code.Influencer.InfluencerErrorResponseCode;
import com.likelion.bd.global.response.code.businessMan.BusinessManErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProposalServiceImpl implements ProposalService {

    private final BusinessManRepository businessManRepository;
    private final InfluencerRepository influencerRepository;
    private final ProposalRepository proposalRepository;

    @Override
    @Transactional(readOnly = true)
    public ProposalFormRes getProposalForm(UserPrincipal userPrincipal) {

        ProposalFormRes.DefaultInfo defaultInfo;

        if (UserRoleType.valueOf(userPrincipal.getRole()) == UserRoleType.BUSINESS) { // 자영업자
            BusinessMan businessMan = businessManRepository.findByUserUserId(userPrincipal.getId())
                    .orElseThrow(() -> new CustomException(BusinessManErrorResponseCode.BUSINESSMAN_NOT_FOUND_404));

            defaultInfo = new ProposalFormRes.BusinessManInfo(
                    businessMan.getUser().getName(),
                    businessMan.getWorkPlace().getName(),
                    businessMan.getWorkPlace().getAddress()
            );
        } else { // 인플루언서
            Influencer influencer = influencerRepository.findByUserUserId(userPrincipal.getId())
                    .orElseThrow(() -> new CustomException(InfluencerErrorResponseCode.INFLUENCER_NOT_FOUND_404));

            List<String> platforms = influencer.getActivity().getActivityPlatformList().stream()
                    .map(ap -> ap.getPlatform().getName())
                    .toList();

            defaultInfo = new ProposalFormRes.InfluencerInfo(
                    influencer.getUser().getName(),
                    influencer.getActivity().getActivityName(),
                    platforms
            );
        }

        Optional<Proposal> optionalProposal = proposalRepository.findByWriterId(userPrincipal.getId());

        Optional<ProposalFormRes.ExistInfo> existInfo = optionalProposal.
                map(proposal -> {
                    String contentTopic =
                            (UserRoleType.valueOf(userPrincipal.getRole()) == UserRoleType.INFLUENCER)
                                    ? proposal.getContentTopic() : null;

                    return new ProposalFormRes.ExistInfo(
                            proposal.getTitle(),
                            proposal.getOfferAmount(),
                            proposal.getStartDate().toString(),
                            proposal.getEndDate().toString(),
                            proposal.getOverView(),
                            proposal.getRequest(),
                            contentTopic
                    );
                });

        return new ProposalFormRes(defaultInfo, existInfo);
    }

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
