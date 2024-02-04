package com.prgrms.be.domain.user.oauth2;

// 각 소셜에서 받아오는 데이터 다름 -> 소셜별로 데이터 받는 데이터를 분기 처리

import com.prgrms.be.domain.user.domain.entity.User;
import com.prgrms.be.domain.user.domain.enums.Role;
import com.prgrms.be.domain.user.domain.enums.SocialType;
import com.prgrms.be.domain.user.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.prgrms.be.domain.user.oauth2.userinfo.KakaoOAuth2UserInfo;
import com.prgrms.be.domain.user.oauth2.userinfo.NaverOAuth2UserInfo;
import com.prgrms.be.domain.user.oauth2.userinfo.OAuth2UserInfo;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthDto {

    private String nameAttributeKey; //OAuth2 로그인 진행 시 키 되는 필드값, PK
    private OAuth2UserInfo oAuth2UserInfo; //소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 ..)

    @Builder
    private OAuthDto(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    // SocialType에 맞는 메소드 호출 -> OAuthDto 객체 반환
    // nameAttributeKey : CustomOAuth2UserService에서 값 얻음
    // attributes: OAuth의 유저 정보들
    public static OAuthDto of(SocialType socialType, String nameAttributeKey,
        Map<String, Object> attributes) {
        if (socialType == SocialType.NAVER) {
            return ofNaver(nameAttributeKey, attributes);
        }
        if (socialType == SocialType.KAKAO) {
            return ofKakao(nameAttributeKey, attributes);
        }

        return ofGoogle(nameAttributeKey, attributes);
    }

    public static OAuthDto ofNaver(String nameAttributeKey, Map<String, Object> attributes) {
        return OAuthDto.builder()
            .nameAttributeKey(nameAttributeKey)
            .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
            .build();
    }

    public static OAuthDto ofKakao(String nameAttributeKey, Map<String, Object> attributes) {
        return OAuthDto.builder()
            .nameAttributeKey(nameAttributeKey)
            .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
            .build();
    }

    public static OAuthDto ofGoogle(String nameAttributeKey, Map<String, Object> attributes) {
        return OAuthDto.builder()
            .nameAttributeKey(nameAttributeKey)
            .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
            .build();
    }

    public User toUser(SocialType socialType, OAuth2UserInfo oAuth2UserInfo) {
        return User.builder()
            .socialType(socialType)
            .socialId(oAuth2UserInfo.getId())
            .email(generateRandomEmail()) // JWT 토큰 발급하기 위한 용도뿐, 임시
            .nickname(oAuth2UserInfo.getNickname())
            .image(oAuth2UserInfo.getImage())
            .role(Role.GUEST)
            .build();
    }

    private static String generateRandomEmail() {
        return UUID.randomUUID() + "@socialUser.com";
    }
}