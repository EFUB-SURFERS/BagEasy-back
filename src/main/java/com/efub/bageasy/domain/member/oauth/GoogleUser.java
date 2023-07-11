package com.efub.bageasy.domain.member.oauth;

import lombok.Data;

//구글(서드파티)로 액세스 토큰을 보내 받아올 구글에 등록된 사용자 정보
@Data
public class GoogleUser {
    public String sub;
    public String email;
    public Boolean email_verified;
    public String name;
    public String given_name;
    public String family_name;
    public String picture;
    public String locale;
    public String hd;
}