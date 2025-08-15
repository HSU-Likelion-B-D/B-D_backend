package com.likelion.bd.domain.campaign.service;

import com.likelion.bd.domain.campaign.entity.Campaign;
import com.likelion.bd.domain.campaign.entity.CampaignStatus;
import com.likelion.bd.domain.campaign.entity.Proposal;
import com.likelion.bd.domain.campaign.repository.CampaignRepository;
import com.likelion.bd.domain.campaign.repository.ProposalRepository;
import com.likelion.bd.domain.campaign.web.dto.CampaignListRes;
import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.domain.user.entity.UserRoleType;
import com.likelion.bd.domain.user.repository.UserRepository;
import com.likelion.bd.global.jwt.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CampaignServiceImplTest {

    @InjectMocks
    private CampaignServiceImpl campaignService;

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProposalRepository proposalRepository;

    private User user;
    private UserPrincipal userPrincipal;
    private List<Campaign> campaignList;

    @BeforeEach
    void setUp() {
        // 사용자 설정
        user = User.builder()
                .userId(1L)
                .email("test@example.com")
                .password("password")
                .name("Test User")
                .profileImage("profile.jpg")
                .role(UserRoleType.BUSINESS)
                .build();

        userPrincipal = new UserPrincipal(user.getUserId(), user.getRole().name());

        // 여러 캠페인 더미 데이터 생성
        campaignList = new ArrayList<>();

        // 캠페인 1: 제안 받음 상태
        Proposal proposal1 = Proposal.builder().proposalId(1L).writerId(user.getUserId()).title("맛있는 빵집 홍보 캠페인").minAmount(100000L).maxAmount(200000L).startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(10)).overView("오픈 기념 빵집 홍보").build();
        Campaign campaign1 = Campaign.builder().campaignId(1L).senderId(user.getUserId()).senderRole(user.getRole().name()).receiverId(2L).receiverRole(UserRoleType.INFLUENCER.name()).proposal(proposal1).state(CampaignStatus.PROPOSED).build();

        // 캠페인 2: 완료 상태
        Proposal proposal2 = Proposal.builder().proposalId(2L).writerId(user.getUserId()).title("여름 옷 쇼핑몰 캠페인").minAmount(500000L).maxAmount(1000000L).startDate(LocalDate.now().minusDays(20)).endDate(LocalDate.now().minusDays(10)).overView("여름 신상 의류 홍보").build();
        Campaign campaign2 = Campaign.builder().campaignId(2L).senderId(user.getUserId()).senderRole(user.getRole().name()).receiverId(3L).receiverRole(UserRoleType.INFLUENCER.name()).proposal(proposal2).state(CampaignStatus.COMPLETED).build();

        // 캠페인 3: 제안 받음 상태
        Proposal proposal3 = Proposal.builder().proposalId(3L).writerId(user.getUserId()).title("신규 카페 홍보").minAmount(50000L).maxAmount(150000L).startDate(LocalDate.now().plusDays(5)).endDate(LocalDate.now().plusDays(15)).overView("분위기 좋은 신규 카페 홍보").build();
        Campaign campaign3 = Campaign.builder().campaignId(3L).senderId(user.getUserId()).senderRole(user.getRole().name()).receiverId(4L).receiverRole(UserRoleType.INFLUENCER.name()).proposal(proposal3).state(CampaignStatus.PROPOSED).build();

        campaignList.add(campaign1);
        campaignList.add(campaign2);
        campaignList.add(campaign3);
    }

    @Test
    @DisplayName("캠페인 목록 조회 성공 - all=true (모든 상태)")
    void showCampaign_AllCampaigns() {
        // given
        Pageable pageable = PageRequest.of(0, 5);
        Page<Campaign> campaignPage = new PageImpl<>(campaignList, pageable, campaignList.size());

        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(campaignRepository.findMyCampaignsAll(eq(userPrincipal.getId()), eq(userPrincipal.getRole()), any(Pageable.class)))
                .thenReturn(campaignPage);

        // when
        Page<CampaignListRes> result = campaignService.showCampaign(userPrincipal, null, true, pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent().size()).isEqualTo(3);

        // =================================================================
        //              조회된 캠페인 정보 출력하는 코드
        // =================================================================
        System.out.println("\n========= 조회된 캠페인 목록 (상태: ALL) =========");
        if (result.isEmpty()) {
            System.out.println("=> 조회된 캠페인이 없습니다.");
        } else {
            result.getContent().forEach(results -> {
                System.out.println("  이미지 URL: " + results.imgUrl());
                System.out.println("  제목: " + results.title());
                System.out.println("  최소 예산: " + results.minBudget());
                System.out.println("  최대 예산: " + results.maxBudget());
                System.out.println("  시작일: " + results.startDate());
                System.out.println("  종료일: " + results.endDate());
                System.out.println("  상태: " + results.status());
                System.out.println("  ---------------------------------");
            });
        }
        System.out.println("==============================================\n");
    }

    @Test
    @DisplayName("여러 캠페인 조회 및 출력 - all=false (특정 상태)")
    void showCampaign_CampaignsByState_WithMultipleData() {
        // given
        CampaignStatus status = CampaignStatus.PROPOSED;
        Pageable pageable = PageRequest.of(0, 5);

        // 테스트 데이터에서 PROPOSED 상태인 캠페인만 필터링
        List<Campaign> proposedCampaigns = campaignList.stream()
                .filter(c -> c.getState() == status)
                .collect(Collectors.toList());

        Page<Campaign> campaignPage = new PageImpl<>(proposedCampaigns, pageable, proposedCampaigns.size());

        when(userRepository.findById(userPrincipal.getId())).thenReturn(Optional.of(user));
        when(campaignRepository.findMyCampaignsByState(eq(userPrincipal.getId()), eq(userPrincipal.getRole()), eq(status), any(Pageable.class)))
                .thenReturn(campaignPage);

        // when
        Page<CampaignListRes> resultPage = campaignService.showCampaign(userPrincipal, status, false, pageable);

        // then
        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getTotalElements()).isEqualTo(2);
        assertThat(resultPage.getContent().get(0).title()).isEqualTo("맛있는 빵집 홍보 캠페인");
        assertThat(resultPage.getContent().get(1).title()).isEqualTo("신규 카페 홍보");


        // =================================================================
        //              조회된 캠페인 정보 출력하는 코드
        // =================================================================
        System.out.println("\n========= 조회된 캠페인 목록 (상태: " + status + ") =========");
        if (resultPage.isEmpty()) {
            System.out.println("=> 조회된 캠페인이 없습니다.");
        } else {
            resultPage.getContent().forEach(campaign -> {
                System.out.println("  이미지 URL: " + campaign.imgUrl());
                System.out.println("  제목: " + campaign.title());
                System.out.println("  최소 예산: " + campaign.minBudget());
                System.out.println("  최대 예산: " + campaign.maxBudget());
                System.out.println("  시작일: " + campaign.startDate());
                System.out.println("  종료일: " + campaign.endDate());
                System.out.println("  상태: " + campaign.status());
                System.out.println("  ---------------------------------");
            });
        }
        System.out.println("==============================================\n");

        verify(campaignRepository).findMyCampaignsByState(eq(userPrincipal.getId()), eq(userPrincipal.getRole()), eq(status), any(Pageable.class));
    }
}