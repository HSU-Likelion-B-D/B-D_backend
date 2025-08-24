package com.likelion.bd.domain.campaign.service;

import com.likelion.bd.domain.businessman.entity.BusinessMan;
import com.likelion.bd.domain.businessman.repository.BusinessManRepository;
import com.likelion.bd.domain.campaign.entity.*;
import com.likelion.bd.domain.campaign.repository.PaymentRepository;
import com.likelion.bd.domain.campaign.web.dto.PaymentListRes;
import com.likelion.bd.domain.campaign.web.dto.PaymentResponseReq;
import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.domain.user.entity.UserRoleType;
import com.likelion.bd.domain.user.repository.UserRepository;
import com.likelion.bd.global.exception.CustomException;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.code.businessMan.BusinessManErrorResponseCode;
import com.likelion.bd.global.response.code.campaign.PaymentErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final int FEE = 10;

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final BusinessManRepository businessManRepository;

    @Override
    @Transactional
    public void createPayment(Campaign campaign) {
        Payment payment = Payment.builder()
                .businessManState(PaymentStatus.PAYMENT_PENDING)
                .influencerState(PaymentStatus.WAITING)
                .fee(FEE)
                .campaign(campaign)
                .build();

        paymentRepository.save(payment);
    }

    @Override
    public Page<PaymentListRes> showPayment(
            UserPrincipal userPrincipal,
            PaymentStatus status,
            Boolean all,
            Pageable pageable
    ) {

        Page<Payment> payments;
        String userRole = userPrincipal.getRole();
        // Repository를 호출하여 사용자와 관련된 Payment 목록을 Page 형태로 가져온다.
        if (userRole.equals(UserRoleType.BUSINESS.toString())) {
            payments = paymentRepository.findPaymentsForBusiness(
                    userPrincipal.getId(),
                    status,
                    all,
                    pageable
            );
        } else {
            payments = paymentRepository.findPaymentsForInfluencer(
                    userPrincipal.getId(),
                    status,
                    all,
                    pageable
            );
        }

        // Page 객체의 .map() 기능을 사용해 Page<Payment>를 Page<PaymentListRes>로 변환한다.
        return payments.map(payment -> {
            // JOIN FETCH로 가져왔기 때문에 추가 쿼리 없이 바로 사용 가능
            Campaign campaign = payment.getCampaign();
            Proposal proposal = campaign.getProposal();

            User user = userPrincipal.getId().equals(campaign.getSenderId()) ?
                    userRepository.getUserByUserId(campaign.getReceiverId()) :
                    userRepository.getUserByUserId(campaign.getSenderId());

            // 실 납부금액 계산
            String offerBudget = proposal.getOfferBudget();
            long longOfferBudget = Long.parseLong(offerBudget.replace(",", "")); // "330,000" -> "330000"
            int fee = payment.getFee();

            long totalPaid;
            String myStatus;
            Optional<PaymentListRes.reviewInfo> reviewInfo = Optional.empty();
            if (userRole.equals(UserRoleType.BUSINESS.toString())) {
                totalPaid = longOfferBudget / 100 * fee + longOfferBudget;

                myStatus = payment.getBusinessManState().getDescription();
            } else {
                totalPaid = longOfferBudget - (longOfferBudget / 100 * fee);

                myStatus = payment.getInfluencerState().getDescription();

                BusinessMan businessMan = businessManRepository.findByUserUserId(user.getUserId())
                        .orElseThrow(() -> new CustomException(BusinessManErrorResponseCode.BUSINESSMAN_NOT_FOUND_404));

                reviewInfo = Optional.of(new PaymentListRes.reviewInfo(
                        businessMan.getWorkPlace().getName(),
                        businessMan.getUser().getIntroduction()
                ));
            }

            // 최종적으로 DTO를 생성하여 반환한다.
            return new PaymentListRes(
                    payment.getPaymentId(),
                    user.getProfileImage(),
                    proposal.getTitle(),
                    offerBudget,
                    fee,
                    totalPaid,
                    proposal.getStartDate().toString(),
                    proposal.getEndDate().toString(),
                    myStatus,
                    user.getUserId(),
                    reviewInfo
            );
        });
    }

    @Override
    @Transactional
    public void updatePayment( // 추후 실제 결제 기능을 넣고 나면 변경될 예정
            UserPrincipal userPrincipal,
            PaymentResponseReq paymentResponseReq
    ) {
        Payment payment = paymentRepository.findById(paymentResponseReq.getPaymentId())
                .orElseThrow(() -> new CustomException(PaymentErrorResponseCode.PAYMENT_NOT_FOUND_404));

        Campaign campaign = payment.getCampaign();

        if (userPrincipal.getRole().equals(UserRoleType.BUSINESS.toString())) { // 자영업자 일 때
            // 돈을 지불한 상황으로 가정
            payment.updateBState(PaymentStatus.PAYMENT_COMPLETED);
            payment.updateIState(PaymentStatus.PAYMENT_DUE);
        } else { // 인플루언서 일 때
            payment.updateIState(PaymentStatus.COMPLETED);
            payment.updateBState(PaymentStatus.COMPLETED);
        }
    }
}
