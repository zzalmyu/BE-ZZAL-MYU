package com.prgrms.zzalmyu.domain.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.domain.enums.Role;
import com.prgrms.zzalmyu.domain.user.domain.enums.SocialType;
import com.prgrms.zzalmyu.domain.user.infrastructure.UserRepository;
import java.util.Optional;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    User saved;

    @BeforeEach
    public void beforeEach() {
        User kazuha = User.builder()
            .role(Role.USER)
            .email("kazuha@naver.com")
            .nickname("카즈하")
            .socialId("소셜아이디")
            .socialType(SocialType.NAVER)
            .build();
        saved = userRepository.save(kazuha);
    }

    @AfterEach
    public void afterEach() {
        userRepository.delete(saved);
    }

    @Test
    @DisplayName("사용자가 탈퇴를 할 수 있다.")
    public void withdraw() {
        userService.withdraw(saved.getId());

        Optional<User> withdraw = userRepository.findById(saved.getId());

        assertThat(withdraw.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("사용자를 id로 찾을 수 있다.")
    public void findUserById() {
        User user = userService.findUserById(saved.getId());

        assertThat(user.getId()).isEqualTo(saved.getId());
    }
}