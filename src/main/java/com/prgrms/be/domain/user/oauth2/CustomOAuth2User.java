package com.prgrms.be.domain.user.oauth2;

import com.prgrms.be.domain.user.domain.enums.Role;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

// 처음 로그인 시 Resource Server가 제공하지 않는 정보를 우리 서비스에서 사용자로부터 입력받기 위해 커스텀
// email: 어떤 유저가 OAuth 로그인을 처음 한 건지 판단하기 위해 추가
//        OAuth 로그인 시 임의의 email 생성해 AccessToken 발급 받아 회원 식별용으로 AccessToken 사용
//        -> OAuth2LoginSuccessHandler에서 해당 이메일로 Token 발급 & 처리
// role: 처음 로그인하는 유저를 Role.GUEST로 설정 -> 추가 정보 입력해 회원가입 진행하면 Role.USER로 업데이트
//       -> OAuth 로그인 회원 중 Role.GUEST인 회원은 SuccessHandler에서 추가정보 입력하는 URL로 리다이렉트
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private String email;
    private Role role;
    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */
    public CustomOAuth2User(
        Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> attributes, String nameAttributeKey
    ,String email, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.role = role;
    }
}
