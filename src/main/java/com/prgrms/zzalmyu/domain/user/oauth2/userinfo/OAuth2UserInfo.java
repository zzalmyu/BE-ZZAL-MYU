package com.prgrms.zzalmyu.domain.user.oauth2.userinfo;

import java.util.Map;

// OAuth 서비스에서 받아온 정보
public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId(); //소셜 식별값: 구글 - "sub", 카카오 - "id", 네이버 - "id"

    public abstract String getNickname();

    public abstract String getEmail();
}
