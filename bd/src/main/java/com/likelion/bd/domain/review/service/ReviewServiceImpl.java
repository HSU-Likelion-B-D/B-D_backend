package com.likelion.bd.domain.review.service;

import com.likelion.bd.domain.businessman.entity.BusinessMan;
import com.likelion.bd.domain.businessman.repository.BusinessManRepository;
import com.likelion.bd.domain.campaign.entity.Payment;
import com.likelion.bd.domain.campaign.repository.PaymentRepository;
import com.likelion.bd.domain.influencer.entity.Influencer;
import com.likelion.bd.domain.influencer.repository.InfluencerRepository;
import com.likelion.bd.domain.review.entity.Review;
import com.likelion.bd.domain.review.repository.ReviewRepository;
import com.likelion.bd.domain.review.web.dto.ReviewCreateReq;
import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.domain.user.entity.UserRoleType;
import com.likelion.bd.domain.user.repository.UserRepository;
import com.likelion.bd.global.exception.CustomException;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.code.Influencer.InfluencerErrorResponseCode;
import com.likelion.bd.global.response.code.businessMan.BusinessManErrorResponseCode;
import com.likelion.bd.global.response.code.campaign.PaymentErrorResponseCode;
import com.likelion.bd.global.response.code.user.UserErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BusinessManRepository businessManRepository;
    private final InfluencerRepository influencerRepository;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void createReview(ReviewCreateReq reviewCreateReq, UserPrincipal userPrincipal) {
        // 작성자 조회
        Long writerId = userPrincipal.getId();
        UserRoleType writerRole = UserRoleType.valueOf(userPrincipal.getRole());

        Payment payment = paymentRepository.findById(reviewCreateReq.getPaymentId())
                .orElseThrow(() -> new CustomException(PaymentErrorResponseCode.PAYMENT_NOT_FOUND_404));

        // 리뷰 대상 조회
        User reviewed = userRepository.findByUserId(reviewCreateReq.getReviewedId())
                .orElseThrow(() -> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        if (reviewed.getRole() == UserRoleType.BUSINESS) {
            BusinessMan businessMan = businessManRepository.findByUserUserId(reviewCreateReq.getReviewedId())
                    .orElseThrow(() -> new CustomException(BusinessManErrorResponseCode.BUSINESSMAN_NOT_FOUND_404));

            businessMan.addReview(reviewCreateReq.getScore());
        } else {
            Influencer influencer = influencerRepository.findByUserUserId(reviewCreateReq.getReviewedId())
                    .orElseThrow(() -> new CustomException(InfluencerErrorResponseCode.INFLUENCER_NOT_FOUND_404));

            influencer.addReview(reviewCreateReq.getScore());
        }

        Review review = Review.builder()
                .writerId(writerId)
                .writeRole(writerRole)
                .reviewedId(reviewed.getUserId())
                .reviewedRole(reviewed.getRole())
                .score(reviewCreateReq.getScore())
                .content(reviewCreateReq.getContent())
                .build();

        reviewRepository.save(review);

        payment.updateTF(true);
    }
}
