package com.digierp.it.cas.social.wechat;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class WeChatUserInfo implements OAuth2User {

    // 统一赋予USER角色
    private List<GrantedAuthority> authorities =
            AuthorityUtils.createAuthorityList("USER");
    private Map<String, Object> attributes;

    //	普通用户的标识，对当前开发者账号唯一
    private String openid;
    //	普通用户昵称
    private String nickname;
    //	普通用户性别，1为男性，2为女性
    private String sex;
    //	普通用户个人资料填写的省份
    private String province;
    //	普通用户个人资料填写的城市
    private String city;
    //	国家，如中国为CN
    private String country;
    //	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
    private String headimgurl;
    //	用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
    private List<String> privilege;
    //	用户统一标识。针对一个微信开放平台账号下的应用，同一用户的unionid是唯一的。
    private String unionid;

    private String language;

    @Override
    public Map<String, Object> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
            this.attributes.put("nickname", this.getNickname());
            this.attributes.put("sex", this.getSex());
            this.attributes.put("province", this.getProvince());
            this.attributes.put("city", this.getCity());
            this.attributes.put("country", this.getCountry());
            this.attributes.put("headimgurl", this.getHeadimgurl());
            this.attributes.put("privilege", this.getPrivilege());
            this.attributes.put("openId", this.getOpenid());
            this.attributes.put("language", this.getLanguage());
        }
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.nickname;
    }

    public void setNickname(String nickname) throws UnsupportedEncodingException {
        this.nickname = new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
    }
}
