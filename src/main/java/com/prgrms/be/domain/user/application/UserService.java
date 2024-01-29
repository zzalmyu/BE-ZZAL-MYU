package com.prgrms.be.domain.user.application;

import com.prgrms.be.domain.user.domain.entity.User;
import com.prgrms.be.domain.user.infrastructure.UserJPARepository;
import com.prgrms.be.domain.user.presentation.dto.req.UserSignUpRequest;
import com.prgrms.be.domain.user.presentation.dto.res.UserSignUpResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserJPARepository userJPARepository;
    private final PasswordEncoder passwordEncoder;

    public UserSignUpResponse signUp(UserSignUpRequest request) {
        checkEmailDuplication(request.getEmail());
        checkNicknameDuplication(request.getNickname());

        User user = userJPARepository.save(request.toUserEntity(passwordEncoder));
        return UserSignUpResponse.from(user);
    }

    private void checkEmailDuplication(String email) {
        userJPARepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new RuntimeException("이미 존재하는 이메일입니다.");
                });
    }

    private void checkNicknameDuplication(String nickname) {
        userJPARepository.findByNickname(nickname)
                .ifPresent(user -> {
                    throw new RuntimeException("이미 존재하는 닉네임입니다.");
                });
    }
}
