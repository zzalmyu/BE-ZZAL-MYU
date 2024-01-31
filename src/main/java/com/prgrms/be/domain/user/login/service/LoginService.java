package com.prgrms.be.domain.user.login.service;

import com.prgrms.be.domain.user.domain.entity.User;
import com.prgrms.be.domain.user.infrastructure.UserJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 1. DaoAuthenticationProvider는 UserDetailService의 loadUserByUsername을 호출해 UserDetails 객체를 반환 받음
//      - db에 유저가 있다면 찾은 user로 UserDetails 객체를 만들어 반환
// 2. DaoAuthenticationProvider는 1에서 반환받은 UserDetails 객체의 password를 꺼내 내부의 PasswordEncoder에서 token으로 넘어온 password와 일치하는지 검증 수행
// 3. 비밀번호 일치 -> UsernamePasswordAuthenticationToken에 UserDetails 객체와 Authorities 담아서 반환
//      -> ProviderManager에서 반환된 UserPasswordAuthenticationToken을 이용해 인증 객체를 생성하고, SecurityContext에 저장(인증 성공 처리)
// 4. 로그인 성공 -> LoginSuccessHandler 고고
//    로그인 실패 -> LoginFailureHandler 고고
@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserJPARepository userJPARepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userJPARepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));

        return org.springframework.security.core.userdetails.User.builder() //UserDetails의 User에서 구현한 builder -> role이 "ROLE_"로 시작하지 않으면 예외 발생시킴
            .username(user.getEmail())
            .password(user.getPassword())
            .roles(user.getRole().name())
            .build();
    }
}
