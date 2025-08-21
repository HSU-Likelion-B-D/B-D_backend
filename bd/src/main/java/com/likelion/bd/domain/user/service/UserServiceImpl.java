package com.likelion.bd.domain.user.service;

import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.domain.user.entity.UserRoleType;
import com.likelion.bd.domain.user.exception.*;
import com.likelion.bd.domain.user.repository.UserRepository;
import com.likelion.bd.domain.user.web.dto.*;
import com.likelion.bd.global.exception.CustomException;
import com.likelion.bd.global.external.s3.S3Service;
import com.likelion.bd.global.jwt.JwtTokenProvider;
import com.likelion.bd.global.jwt.UserPrincipal;
import com.likelion.bd.global.mail.MailService;
import com.likelion.bd.global.response.code.ErrorResponseCode;
import com.likelion.bd.global.response.code.user.UserErrorResponseCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final S3Service s3Service;
    private final JwtTokenProvider jwtTokenProvider;
    private final MailService mailService;

    // 이메일 중복 검사
    @Override
    public void checkEmail(CheckEmailReq checkEmailReq) {
        if (userRepository.existsByEmail(checkEmailReq.getEmail())) {
            throw new DuplicateEmailException();
        }
    }

    // 닉네임 중복 검사
    @Override
    public void checkNickname(CheckNicknameReq checkNicknameReq) {
        if (userRepository.existsByNickname(checkNicknameReq.getNickname())) {
            throw new DuplicateNicknameException();
        }
    }

    // 회원가입
    @Override
    @Transactional
    public UserSignupRes signup(UserSignupReq userSignupReq) {

        UserRoleType role = UserRoleType.valueOf(userSignupReq.getRole().toUpperCase());

        User user = User.builder()
                .name(userSignupReq.getName())
                .email(userSignupReq.getEmail())
                .password(bCryptPasswordEncoder.encode(userSignupReq.getPassword()))
                .role(role)
                .build();

        // User 저장
        User saveUser = userRepository.save(user);

        // 반환
        return new UserSignupRes(
                saveUser.getUserId(),
                saveUser.getRole()
        );
    }

    // 프로필 생성
    @Override
    @Transactional
    public void profileCreate(ProfileCreateReq profileCreateReq) {

        User user = userRepository.findByUserId(profileCreateReq.getUserId())
                .orElseThrow(() -> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        // s3에 저장된 이미지 url 받아오기
        String imageUrl = null;
        if (profileCreateReq.getProfileImage() != null && !profileCreateReq.getProfileImage().isEmpty()) {
            imageUrl = s3Service.uploadImageToS3(profileCreateReq.getProfileImage());
        }

        user.updateProfile(
                profileCreateReq.getNickname(),
                imageUrl,
                profileCreateReq.getIntroduction()
        );
    }

    // 로그인
    @Override
    public UserSigninRes signin(UserSigninReq userSigninReq) {
        // 회원 존재 여부 확인
        User user = userRepository.findByEmail(userSigninReq.getEmail())
                .orElseThrow(NotFoundEmailException::new);

        // 사용자가 입력한 비밀번호(평문)와 DB에 저장된 암호화된 비밀번호를 비교
        // BCrypt는 같은 비밀번호여도 매번 다른 해시를 생성하기 때문에 단순 equals로 비교할 수 없음
        // matches()는 내부적으로 입력값을 해싱한 뒤, 저장된 해시와 비교하여 true/false를 반환함
        if (!bCryptPasswordEncoder.matches(userSigninReq.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException();
        }

        String token = jwtTokenProvider.createToken(user);

        return new UserSigninRes(
                user.getNickname(),
                user.getProfileImage(),
                user.getRole(),
                token
        );
    }

    @Override
    public UserFormRes getUser(UserPrincipal userPrincipal) {
        User user = userRepository.findByUserId(userPrincipal.getId())
                .orElseThrow(() -> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        return new UserFormRes(
                user.getNickname(),
                user.getProfileImage(),
                user.getIntroduction()
        );
    }

    // 회원 정보 수정
    @Override
    @Transactional
    public void updateUser(UserUpdateReq userUpdateReq, Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        String newNickname = null;
        String newImageUrl = user.getProfileImage();
        String newIntroduction = user.getIntroduction();

        if (userUpdateReq.getNickname() != null && !userUpdateReq.getNickname().isEmpty()) {
            newNickname = userUpdateReq.getNickname();
        }

        if (userUpdateReq.isImageDelete()) {
            if (user.getProfileImage() != null) {
                s3Service.deleteImageFromS3(user.getProfileImage());
            }
            newImageUrl = null;
        }
        if (userUpdateReq.getProfileImage() != null && !userUpdateReq.getProfileImage().isEmpty()) {
            s3Service.deleteImageFromS3(user.getProfileImage());
            newImageUrl = s3Service.uploadImageToS3(userUpdateReq.getProfileImage());
        }

        if (userUpdateReq.isIntroductionDelete()) {
            newIntroduction = null;
        }
        if (userUpdateReq.getIntroduction() != null && !userUpdateReq.getIntroduction().isEmpty()) {
            newIntroduction = userUpdateReq.getIntroduction();
        }

        user.updateProfile(newNickname, newImageUrl, newIntroduction);
    }

    // 이메일로 인증번호 전송
    @Override
    public void sendCodeToEmail(CheckEmailReq checkEmailReq) {
        String email = checkEmailReq.getEmail();

        if (checkEmailReq.getPurpose().equals("SIGNUP")) {
            // 회원가입할 때는 "신규" 메일이여야함
            checkEmail(checkEmailReq);
        } else if (checkEmailReq.getPurpose().equals("PW_CHANGE")) {
            // 비밀번호 변경때는 "가입된" 메일이여함.
            if (!userRepository.existsByEmail(checkEmailReq.getEmail())) {
                throw new NotFoundEmailException();
            }
        } else {
            throw new CustomException(ErrorResponseCode.BAD_REQUEST_ERROR);
        }

        mailService.sendAuthCode(email);
    }

    // 인증번호 검증
    @Override
    public void verifyCode(CheckEmailReq checkEmailReq) {
        String email = checkEmailReq.getEmail();
        String code = checkEmailReq.getCode();

        mailService.verifyCode(email, code);
    }

    // 비밀번호 변경
    @Override
    @Transactional
    public void changePassword(UserSigninReq userSigninReq) {
        User user = userRepository.findByEmail(userSigninReq.getEmail())
                .orElseThrow(NotFoundEmailException::new);

        user.changePassword(bCryptPasswordEncoder.encode(userSigninReq.getPassword()));
    }
}
