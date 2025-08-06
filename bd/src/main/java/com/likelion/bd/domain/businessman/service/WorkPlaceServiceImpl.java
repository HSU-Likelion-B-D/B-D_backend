package com.likelion.bd.domain.businessman.service;

import com.likelion.bd.domain.businessman.entity.BusinessMan;
import com.likelion.bd.domain.businessman.entity.Category;
import com.likelion.bd.domain.businessman.entity.WorkPlace;
import com.likelion.bd.domain.businessman.entity.WorkPlaceCategory;
import com.likelion.bd.domain.businessman.repository.*;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateReq;
import com.likelion.bd.domain.businessman.web.dto.WorkPlaceCreateRes;
import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.domain.user.entity.UserRoleType;
import com.likelion.bd.domain.user.repository.UserRepository;
import com.likelion.bd.global.exception.CustomException;
import com.likelion.bd.global.response.code.BusinessManErrorResponseCode;
import com.likelion.bd.global.response.code.UserErrorResponseCode;
import com.likelion.bd.global.response.code.WorkPlaceErrorReponseCode;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow( () -> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        //해당 API는 자영업자를 위한 것이므로 User의 역할이 BUSINESS가 아니라면 접근불가 예외처리를 해줌
        if(user.getRole() != UserRoleType.BUSINESS){
            throw new CustomException(UserErrorResponseCode.NO_BUSINESS_PERMISSION_403);
        }

        //사업자 엔티티를 찾기
        //BusinessMan을 작성 시 User를 외래키로 관리하면 userId로 BusinessMan을 찾는 쿼리가 필요할 수 있음
        //아래와 같다.
        BusinessMan businessMan = businessManRepository.findByUser(user)
                .orElseThrow( () -> new CustomException(BusinessManErrorResponseCode.BUSINESSMAN_NOT_FOUND_404));

        //빌더패턴에선 직접적으로 try-catch를 사용하지 못하므로 사전에 openTime,closeTime을 검증해줘야한다.
        LocalTime openTime;
        LocalTime closeTime;
        try{
            openTime = LocalTime.parse(workPlaceCreateReq.getOpenTime());
            closeTime = LocalTime.parse(workPlaceCreateReq.getCloseTime());
        }catch (DateTimeParseException e){
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
                .onlineStore(workPlaceCreateReq.getIsOnline())
                .build();


        //업장의 카테고리 매핑
        if(workPlaceCreateReq.getCategoryIds() != null){
            for(Long categoryId : workPlaceCreateReq.getCategoryIds()){
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow( () -> new CustomException(WorkPlaceErrorReponseCode.CATEGORY_NOT_FOUND_404));
                WorkPlaceCategory wpc = new WorkPlaceCategory(workPlace, category);
                workPlace.getCategoryList().add(wpc);
            }
        }

        //업
        return null;
    }
}
