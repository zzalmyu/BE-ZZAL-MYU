package com.prgrms.zzalmyu.domain.user.oauth2;

import com.prgrms.zzalmyu.domain.user.domain.enums.Role;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

// accesstoken을 만들기 위한 이메일을 쉽게 얻기 위해 커스텀
// email: 어떤 유저가 OAuth 로그인을 한 건지 판단하기 위해 추가
//        OAuth 로그인 시 임의의 email 생성해 AccessToken 발급 받아 회원 식별용으로 AccessToken 사용
//        -> OAuth2LoginSuccessHandler에서 해당 이메일로 Token 발급 & 처리
// Authorization Code 인증 성공 -> OAuth2LoginSuccessHandler 고고
// Authorization Code 인증 실패 -> OAuth2LoginFailureHandler 고고
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private String email;

    private String nickname;

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
        , String email, String nickname, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }
}
