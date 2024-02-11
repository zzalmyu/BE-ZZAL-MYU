package com.prgrms.zzalmyu.domain.user.application;

import com.prgrms.zzalmyu.domain.user.infrastructure.UserJPARepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserJPARepository userJPARepository;
}
