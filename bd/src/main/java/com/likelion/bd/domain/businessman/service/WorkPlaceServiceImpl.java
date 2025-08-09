package com.likelion.bd.domain.businessman.service;

import com.likelion.bd.domain.businessman.entity.*;
import com.likelion.bd.domain.businessman.repository.*;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateReq;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateRes;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceUpdateReq;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceUpdateRes;
import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.domain.user.entity.UserRoleType;
import com.likelion.bd.domain.user.repository.UserRepository;
import com.likelion.bd.global.exception.CustomException;
import com.likelion.bd.global.response.code.BusinessManErrorResponseCode;
import com.likelion.bd.global.response.code.ErrorResponseCode;
import com.likelion.bd.global.response.code.UserErrorResponseCode;
import com.likelion.bd.global.response.code.WorkPlaceErrorReponseCode;
import lombok.RequiredArgsConstructor;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class WorkPlaceServiceImpl implements WorkPlaceService {

    private final UserRepository userRepository;
    private final WorkPlaceRepository workPlaceRepository;
    private final CategoryRepository categoryRepository;
    private final MoodRepository moodRepository;
    private final PromotionRepository promotionRepository;
    private final BusinessManRepository businessManRepository;

    @Transactional
    @Override
    public WorkPlaceCreateRes createWorkPlace(WorkPlaceCreateReq workPlaceCreateReq, Long userId) {

        //User 조회 userId를 통해서 User가 우선 존재하는지 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        //해당 API는 자영업자를 위한 것이므로 User의 역할이 BUSINESS가 아니라면 접근불가 예외처리를 해줌
        if (user.getRole() != UserRoleType.BUSINESS) {
            throw new CustomException(UserErrorResponseCode.NO_BUSINESS_PERMISSION_403);
        }

        //사업자 엔티티를 찾기
        //BusinessMan을 작성 시 User를 외래키로 관리하면 userId로 BusinessMan을 찾는 쿼리가 필요할 수 있음
        //아래와 같다.
        BusinessMan businessMan = businessManRepository.findByUser(user)
                .orElseGet(() -> {
                    // 이 블록은 "없을 때만 실행"
                    BusinessMan bm = BusinessMan.builder().user(user).build();
                    return businessManRepository.save(bm);
                });
// 여기서 bm은 무조건 존재 (기존 or 신규)


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
                .businessman(businessMan)
                .name(workPlaceCreateReq.getName())
                .address(workPlaceCreateReq.getAddress())
                .detailAddress(workPlaceCreateReq.getDetailAddress())
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
        WorkPlace saved = workPlaceRepository.save(workPlace);
        return new WorkPlaceCreateRes(
                saved.getBusinessman().getBusinessManId(),
                saved.getWorkplaceId(),
                saved.getName(),
                saved.getAddress(),
                //LocalTime에서 String으로 변환하면 자동으로 HH:MM형태로 파싱된다.
                saved.getOpenTime().toString(),
                saved.getCloseTime().toString(),
                saved.getIsOnline(),

                //중간매핑테이블에서 각 카테고리, 분위기, 홍보방식의 id만 추출해서 전달한다.
                // 1. saved.getCategoryList()로 WorkPlaceCategory와 같은 중간매핑테이블을 가져온다.
                // 2. 각각의 중간매핑테이블에서 각각 마스터테이블(Category, Mood, Promotion)의 id만 추출
                // 3. 결과를 List<Long> 형태로 만들어준다.
                saved.getCategoryList().stream().map(wpc -> wpc.getCategory().getId()).toList(),
                saved.getMoodList().stream().map(wpm -> wpm.getMood().getId()).toList(),
                saved.getPromotionList().stream().map(wpp -> wpp.getPromotion().getId()).toList()
        );
    }

    @Transactional
    @Override
    public WorkPlaceUpdateRes updateWorkPlace(WorkPlaceUpdateReq workPlaceUpdateReq, Long workPlaceId) {

        //유저 조회
        User user = userRepository.findById(workPlaceUpdateReq.getUserId())
                .orElseThrow( ()-> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));


        //사업자인지 검증
        if(user.getRole() != UserRoleType.BUSINESS) {
            throw new CustomException(UserErrorResponseCode.NO_BUSINESS_PERMISSION_403);
        }

        //사업자 조회
        BusinessMan businessMan = businessManRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(BusinessManErrorResponseCode.BUSINESSMAN_NOT_FOUND_404));

        //사업장 조회
        WorkPlace workPlace = workPlaceRepository.findById(workPlaceId)
                .orElseThrow(()-> new CustomException(WorkPlaceErrorReponseCode.WORKPLACE_NOT_FOUND_404));

        //사업장에 있는 사업자 id와 위에서 조회한 사업자의 id가 맞는지 확인
        if(!workPlace.getBusinessman().getBusinessManId().equals(businessMan.getBusinessManId())) {
            throw new CustomException(ErrorResponseCode.ACCESS_DENIED_REQUEST);
        }

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

        //새로운 정보 저장
//        workPlace = WorkPlace.builder()
//                .name(workPlaceUpdateReq.getName())
//                .address(workPlaceUpdateReq.getAddress())
//                .detailAddress(workPlaceUpdateReq.getDetailAddress())
//                .openTime(openTime)
//                .closeTime(closeTime)
//                .onlineStore(workPlaceUpdateReq.getIsOnline())
//                .build();
        workPlace.updateBasicInfo(
                workPlaceUpdateReq.getName(),
                workPlaceUpdateReq.getAddress(),
                workPlaceUpdateReq.getDetailAddress(),
                openTime,
                closeTime,
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
                businessMan.getBusinessManId(),
                workPlace.getName(),
                workPlace.getAddress(),
                workPlace.getDetailAddress(),
                workPlace.getOpenTime().toString(),
                workPlace.getCloseTime().toString(),
                workPlace.getIsOnline(),
                workPlace.getCategoryList().stream().map(wpc -> wpc.getCategory().getId()).toList(),
                workPlace.getMoodList().stream().map(wpm -> wpm.getMood().getId()).toList(),
                workPlace.getPromotionList().stream().map(wpp -> wpp.getPromotion().getId()).toList()
        );
    }
}
