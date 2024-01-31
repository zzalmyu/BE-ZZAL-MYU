package com.prgrms.be.domain.user.infrastructure;

import com.prgrms.be.domain.user.domain.entity.User;
import com.prgrms.be.domain.user.domain.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJPARepository extends JpaRepository<User, Long> {

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByRefreshToken(String refreshToken);
}
