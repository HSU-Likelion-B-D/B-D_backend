package com.likelion.bd.domain.user.service;

import com.likelion.bd.domain.user.entity.User;
import com.likelion.bd.domain.user.entity.UserRoleType;
import com.likelion.bd.domain.user.repository.UserRepository;
import com.likelion.bd.domain.user.web.dto.UserSignupReq;
import com.likelion.bd.domain.user.web.dto.UserSignupRes;
import com.likelion.bd.global.external.s3.S3Service;
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

    @Override
    @Transactional
    public UserSignupRes signup(UserSignupReq userSignupReq) { // 회원가입

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
}
