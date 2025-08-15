package com.likelion.bd.domain.campaign.service;

import com.likelion.bd.domain.campaign.entity.Campaign;
import com.likelion.bd.domain.campaign.entity.CampaignStatus;
import com.likelion.bd.domain.campaign.entity.Proposal;
import com.likelion.bd.domain.campaign.repository.CampaignRepository;
import com.likelion.bd.domain.campaign.repository.ProposalRepository;
import com.likelion.bd.domain.campaign.web.dto.CampaignListRes;
import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.domain.user.repository.UserRepository;
import com.likelion.bd.global.exception.CustomException;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.code.user.UserErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final ProposalRepository proposalRepository;

    @Override
    public Page<CampaignListRes> showCampaign(UserPrincipal userPrincipal, CampaignStatus state, Boolean all,Pageable pageable) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow( ()-> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        Long userId = userPrincipal.getId();
        String role = userPrincipal.getRole();

        // 기본 정렬이 없으면 id DESC로
        Pageable pageReq = (pageable != null && pageable.getSort().isSorted())
                ? pageable
                : PageRequest.of(
                pageable != null ? pageable.getPageNumber() : 0,
                pageable != null ? pageable.getPageSize() : 3,
                Sort.by(Sort.Direction.DESC, "id")
        );

        // 모두보기(all=true)면 상태 무시, 아니면 상태 적용
        Page<Campaign> campaignPage = all
                ? campaignRepository.findMyCampaignsAll(userId, role, pageReq)
                : campaignRepository.findMyCampaignsByState(userId, role, state, pageReq);

        return campaignPage.map(c -> {
            Proposal p = c.getProposal(); // fetch 되어 있음(@EntityGraph)
            String title    = (p != null) ? p.getTitle()     : null;
            Long minBudget  = (p != null) ? p.getMinAmount() : null;
            Long maxBudget  = (p != null) ? p.getMaxAmount() : null;
            LocalDate startDate = (p != null) ? p.getStartDate() : null;
            LocalDate endDate = (p != null) ? p.getEndDate() : null;

            // 상태는 코드 문자열로
            String statusStr = (c.getState() != null) ? c.getState().name() : null;

            return new CampaignListRes(
                    user.getProfileImage(), // imgUrl
                    title,
                    minBudget,
                    maxBudget,
                    startDate,
                    endDate,
                    statusStr
            );
        });
    }
}
