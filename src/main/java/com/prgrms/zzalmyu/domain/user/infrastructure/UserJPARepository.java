package com.prgrms.zzalmyu.domain.user.infrastructure;

import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.domain.enums.SocialType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserJPARepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.socialType = :socialType AND u.socialId = :socialId AND u.deletedAt IS NULL")
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deletedAt IS NULL")
    Optional<User> findByEmail(String email);

    @Override
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deletedAt IS NULL")
    Optional<User> findById(Long id);
}
