package com.likelion.bd.domain.user.service;

import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.domain.user.entity.UserRoleType;
import com.likelion.bd.domain.user.exception.DuplicateEmailException;
import com.likelion.bd.domain.user.exception.DuplicateNicknameException;
import com.likelion.bd.domain.user.exception.InvalidPasswordException;
import com.likelion.bd.domain.user.exception.NotFoundEmailException;
import com.likelion.bd.domain.user.repository.UserRepository;
import com.likelion.bd.domain.user.web.dto.*;
import com.likelion.bd.global.external.s3.S3Service;
import com.likelion.bd.global.jwt.JwtTokenProvider;
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

        // s3에 저장된 이미지 url 받아오기
        String imageUrl = null;
        if (userSignupReq.getProfileImage() != null) {
            imageUrl = s3Service.uploadImageToS3(userSignupReq.getProfileImage());
        }

        User user = User.builder()
                .name(userSignupReq.getName())
                .email(userSignupReq.getEmail())
                .password(bCryptPasswordEncoder.encode(userSignupReq.getPassword()))
                .nickname(userSignupReq.getNickname())
                .profileImage(imageUrl)
                .introduction(userSignupReq.getIntroduction())
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

        return new UserSigninRes(token);
    }
}
