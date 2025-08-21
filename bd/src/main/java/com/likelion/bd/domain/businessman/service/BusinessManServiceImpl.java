package com.likelion.bd.domain.businessman.service;

import com.likelion.bd.domain.businessman.entity.*;
import com.likelion.bd.domain.businessman.repository.*;
import com.likelion.bd.domain.businessman.web.dto.*;
import com.likelion.bd.domain.influencer.entity.Influencer;
import com.likelion.bd.domain.influencer.repository.InfluencerRepository;
import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.domain.user.repository.UserRepository;
import com.likelion.bd.global.exception.CustomException;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.response.code.businessMan.BusinessManErrorResponseCode;
import com.likelion.bd.global.response.code.user.UserErrorResponseCode;
import com.likelion.bd.global.response.code.businessMan.WorkPlaceErrorReponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessManServiceImpl implements BusinessManService {

    private final UserRepository userRepository;
    private final WorkPlaceRepository workPlaceRepository;
    private final CategoryRepository categoryRepository;
    private final MoodRepository moodRepository;
    private final PromotionRepository promotionRepository;
    private final BusinessManRepository businessManRepository;
    private final InfluencerRepository influencerRepository;

    @Transactional
    @Override
    public WorkPlaceCreateRes createWorkPlace(WorkPlaceCreateReq workPlaceCreateReq) {

        //User 조회 userId를 통해서 User가 우선 존재하는지 검증
        User user = userRepository.findById(workPlaceCreateReq.getUserId())
                .orElseThrow(() -> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        //빌더패턴에선 직접적으로 try-catch를 사용하지 못하므로 사전에 openTime,closeTime을 검증해줘야한다.
        LocalTime openTime;
        LocalTime closeTime;
        try {
            openTime = LocalTime.parse(workPlaceCreateReq.getOpenTime());
            closeTime = LocalTime.parse(workPlaceCreateReq.getCloseTime());
        } catch (DateTimeParseException e) {
            throw new CustomException(WorkPlaceErrorReponseCode.INVALID_TIME_FORMAT_400);
        }

        //빌더패턴으로 workPlace 엔티티 생성 및 기본값 저장. 트랜잭션 실행은 하지 않음.
        WorkPlace workPlace = WorkPlace.builder()
                .name(workPlaceCreateReq.getName())
                .address(workPlaceCreateReq.getAddress())
                .detailAddress(workPlaceCreateReq.getDetailAddress())
                .minBudget(workPlaceCreateReq.getMinBudget())
                .maxBudget(workPlaceCreateReq.getMaxBudget())
                .openTime(openTime)
                .closeTime(closeTime)
                .isOnline(workPlaceCreateReq.getIsOnline())
                .build();


        //업장의 카테고리 매핑
        if (workPlaceCreateReq.getCategoryIds() != null) {
            for (Long categoryId : workPlaceCreateReq.getCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CustomException(WorkPlaceErrorReponseCode.CATEGORY_NOT_FOUND_404));
                WorkPlaceCategory wpc = new WorkPlaceCategory(workPlace, category);
                workPlace.getCategoryList().add(wpc);
            }
        }

        //업장 분위기 매핑
        if (workPlaceCreateReq.getMoodIds() != null) {
            for (Long moodId : workPlaceCreateReq.getMoodIds()) {
                Mood mood = moodRepository.findById(moodId)
                        .orElseThrow(() -> new CustomException(WorkPlaceErrorReponseCode.MOOD_NOT_FOUND_404));
                WorkPlaceMood wpm = new WorkPlaceMood(workPlace, mood);
                workPlace.getMoodList().add(wpm);
            }
        }

        //업장 홍보방식 매핑
        if (workPlaceCreateReq.getPromotionIds() != null) {
            for (Long promotionId : workPlaceCreateReq.getPromotionIds()) {
                Promotion promotion = promotionRepository.findById(promotionId)
                        .orElseThrow(() -> new CustomException(WorkPlaceErrorReponseCode.PROMOTION_NOT_FOUND_404));
                WorkPlacePromotion wpp = new WorkPlacePromotion(workPlace, promotion);
                workPlace.getPromotionList().add(wpp);
            }
        }

        //최종 트랜잭션 실행(저장) workPlace만 저장하면 cascade로 인해 중간매핑테이블들도 한번에 저장된다.
        workPlaceRepository.save(workPlace);

        BusinessMan businessMan = BusinessMan.builder()
                .user(user)
                .workPlace(workPlace)
                .totalScore(0.0)
                .reviewCount(0L)
                .build();
        businessManRepository.save(businessMan);

        return new WorkPlaceCreateRes(
                businessMan.getBusinessManId()
        );
    }

    @Override
    public WorkPlaceUpdateInitRes updateWorkPlaceInit(UserPrincipal userPrincipal){
        BusinessMan businessMan = businessManRepository.findByUserUserId(userPrincipal.getId())
                .orElseThrow(()->new CustomException(BusinessManErrorResponseCode.BUSINESSMAN_NOT_FOUND_404));

        User user = businessMan.getUser();
        WorkPlace workPlace = businessMan.getWorkPlace();

        return new WorkPlaceUpdateInitRes(
                workPlace.getName(),
                user.getIntroduction(),
                workPlace.getAddress(),
                workPlace.getDetailAddress(),
                workPlace.getOpenTime().toString(),
                workPlace.getCloseTime().toString(),
                workPlace.getMinBudget().toString(),
                workPlace.getMaxBudget().toString()
        );
    }

    @Transactional
    @Override
    public WorkPlaceUpdateRes updateWorkPlace(WorkPlaceUpdateReq workPlaceUpdateReq, UserPrincipal userPrincipal) {

        //유저 조회
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow( ()-> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        //사업자 조회
        BusinessMan businessMan = businessManRepository.findByUserUserId(userPrincipal.getId())
                .orElseThrow(() -> new CustomException(BusinessManErrorResponseCode.BUSINESSMAN_NOT_FOUND_404));

        //사업장 조회
        WorkPlace workPlace = workPlaceRepository.findById(businessMan.getWorkPlace().getWorkplaceId())
                .orElseThrow(()-> new CustomException(WorkPlaceErrorReponseCode.WORKPLACE_NOT_FOUND_404));

        //기본 정보 수정
        //빌더패턴에선 직접적으로 try-catch를 사용하지 못하므로 사전에 openTime,closeTime을 검증해줘야한다.
        LocalTime openTime;
        LocalTime closeTime;
        try {
            openTime = LocalTime.parse(workPlaceUpdateReq.getOpenTime());
            closeTime = LocalTime.parse(workPlaceUpdateReq.getCloseTime());
        } catch (DateTimeParseException e) {
            throw new CustomException(WorkPlaceErrorReponseCode.INVALID_TIME_FORMAT_400);
        }

        workPlace.updateBasicInfo(
                workPlaceUpdateReq.getName(),
                workPlaceUpdateReq.getAddress(),
                workPlaceUpdateReq.getDetailAddress(),
                openTime,
                closeTime,
                workPlaceUpdateReq.getMinBudget(),
                workPlaceUpdateReq.getMaxBudget(),
                workPlaceUpdateReq.getIsOnline()
        );

        //각 카테고리, 분위기, 홍보방식 리스트 초기화
        workPlace.getCategoryList().clear();
        workPlace.getMoodList().clear();
        workPlace.getPromotionList().clear();

        //새로운 카테고리 저장
        if(workPlaceUpdateReq.getCategoryIds() != null) {
            for (Long categoryId : workPlaceUpdateReq.getCategoryIds()) {
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow( ()-> new CustomException(WorkPlaceErrorReponseCode.CATEGORY_NOT_FOUND_404));
                workPlace.getCategoryList().add(new WorkPlaceCategory(workPlace, category));
            }
        }

        //새로운 분위기 저장
        if(workPlaceUpdateReq.getMoodIds() != null) {
            for (Long moodId : workPlaceUpdateReq.getMoodIds()) {
                Mood mood = moodRepository.findById(moodId)
                        .orElseThrow(()->new CustomException(WorkPlaceErrorReponseCode.MOOD_NOT_FOUND_404));
                workPlace.getMoodList().add(new WorkPlaceMood(workPlace, mood));
            }
        }

        //새로운 홍보방식 저장
        if(workPlaceUpdateReq.getPromotionIds() != null){
            for(Long promotionId : workPlaceUpdateReq.getPromotionIds()){
                Promotion promotion = promotionRepository.findById(promotionId)
                        .orElseThrow(()->new CustomException(WorkPlaceErrorReponseCode.PROMOTION_NOT_FOUND_404));
                workPlace.getPromotionList().add(new WorkPlacePromotion(workPlace,promotion));
            }
        }

        return new WorkPlaceUpdateRes(
                businessMan.getBusinessManId()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public BusinessMyPageRes mypage(UserPrincipal userPrincipal) {

        BusinessMan businessMan = businessManRepository.findByUserUserId(userPrincipal.getId())
                .orElseThrow( ()-> new CustomException(BusinessManErrorResponseCode.BUSINESSMAN_NOT_FOUND_404));

        User user = businessMan.getUser();

        WorkPlace workPlace = businessMan.getWorkPlace();

        List<String> categoryList = workPlace.getCategoryList().stream()
                .map(category -> category.getCategory().getName())
                .toList();

        List<String> moodList = workPlace.getMoodList().stream()
                .map(mood -> mood.getMood().getName())
                .toList();

        List<String> promotionList = workPlace.getPromotionList().stream()
                .map(promotion -> promotion.getPromotion().getName())
                .toList();

        //소수점 2자리까지 처리한다.
        BigDecimal avgScore = BigDecimal.ZERO;
        if (businessMan.getReviewCount() > 0) {
            avgScore = BigDecimal.valueOf(businessMan.getTotalScore())
                    .divide(BigDecimal.valueOf(businessMan.getReviewCount()), 2, RoundingMode.HALF_UP);
        }
        String avgText = String.format("%.2f", avgScore);

        return new BusinessMyPageRes(
                user.getProfileImage(),
                user.getNickname(),
                workPlace.getName(),
                workPlace.getAddress(),
                workPlace.getDetailAddress(),
                user.getIntroduction(),
                //LocalTime에서 String으로 변환하면 자동으로 HH:mm형태로 파싱된다.
                workPlace.getOpenTime().toString(),
                workPlace.getCloseTime().toString(),
                avgText,
                workPlace.getMinBudget(),
                workPlace.getMaxBudget(),
                businessMan.getReviewCount(),
                categoryList,
                moodList,
                promotionList
        );
    }

    @Override
    public BusinessHomeRes home(UserPrincipal userPrincipal) {
        BusinessMan businessMan = businessManRepository.findByUserUserId(userPrincipal.getId())
                .orElseThrow(()->new CustomException(BusinessManErrorResponseCode.BUSINESSMAN_NOT_FOUND_404));

        User user = businessMan.getUser();
        WorkPlace workPlace = businessMan.getWorkPlace();
        String avgText = businessMan.formatAverageScore(businessMan.getTotalScore(), businessMan.getReviewCount());

        Pageable topFour = PageRequest.of(0, 4);
        List<Influencer> topInfluencers = influencerRepository.findTopInfluencerFollowerCountDesc(topFour);
        List<BusinessHomeRes.InfluencerSummaryRes> Influencers = topInfluencers.stream()
                .map(Influencer -> new BusinessHomeRes.InfluencerSummaryRes(
                        Influencer.getUser().getUserId(),
                        Influencer.getUser().getNickname(),
                        Influencer.getUser().getProfileImage(),
                        Influencer.getActivity().formatFollowers()
                ))
                .toList();

        return new BusinessHomeRes(
                user.getProfileImage(),
                user.getNickname(),
                workPlace.getName(),
                avgText,
                businessMan.getReviewCount(),
                Influencers
        );
    }
}
