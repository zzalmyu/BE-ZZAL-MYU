package com.prgrms.zzalmyu.domain.user.oauth2.service;

import com.prgrms.zzalmyu.domain.user.domain.entity.User;
import com.prgrms.zzalmyu.domain.user.domain.enums.SocialType;
import com.prgrms.zzalmyu.domain.user.infrastructure.UserJPARepository;
import com.prgrms.zzalmyu.domain.user.oauth2.CustomOAuth2User;
import com.prgrms.zzalmyu.domain.user.oauth2.OAuthDto;
import com.prgrms.zzalmyu.domain.user.oauth2.userinfo.OAuth2UserInfo;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserJPARepository userJPARepository;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    /*
    CustomOauthUserService 객체 생성 -> loadUser 통해 DefaultOAuth 객체 생성
    CustomOAuthUserSerivce의 loadUser() : 소셜 로그인 API의 사용자 정보 제공 URI로 요청 보내서 사용자 정보 얻음
                                        -> CustomOauth2User 객체 생성
    OAuth2User : OAuth 서비스에서 가져온 유저 정보 담은 객체
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        /*
        userRequest에서 registrationId 추출 -> registrationId로 SocialType 저장
        http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
        userNameAttributeName : 이후에 nameAttributeKey
         */
        SocialType socialType = getSocialType(userRequest);
        String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 유저 정보들의 Json 값

        // SocialType에 따라 유저 정보 통해 OAuthDto 객체 생성
        OAuthDto oAuthDto = OAuthDto.of(socialType, userNameAttributeName, attributes);
        User user = getUser(oAuthDto, socialType);

        return new CustomOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
            // SimpleGrantedAuthority : Spring Security에서 권한 나타내는 구현체
            // Collections.singleton() : 단일 항목으로 갖는 컬렉션 생성
            attributes,
            oAuthDto.getNameAttributeKey(),
            user.getEmail()
        );
        // CustomOAuth2User로 고고
    }

    /*
    회원이 존재한다면 그대로 반환, 없다면 회원 저장
     */
    private User getUser(OAuthDto oAuthDto, SocialType socialType) {
        OAuth2UserInfo oAuth2UserInfo = oAuthDto.getOAuth2UserInfo();
        User findUser = userJPARepository.findBySocialTypeAndSocialId(socialType, oAuth2UserInfo.getId())
            .orElseGet(() -> saveUser(oAuthDto, socialType, oAuth2UserInfo));
        return findUser;
    }

    private User saveUser(OAuthDto oAuthDto, SocialType socialType, OAuth2UserInfo oAuth2UserInfo) {
        User createdUser = oAuthDto.toUser(socialType, oAuth2UserInfo);
        return userJPARepository.save(createdUser);
    }

    private SocialType getSocialType(OAuth2UserRequest userRequest) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if (registrationId.equals(NAVER)) {
            return SocialType.NAVER;
        }
        if (registrationId.equals(KAKAO)) {
            return SocialType.KAKAO;
        }
        return SocialType.GOOGLE;
    }
}
