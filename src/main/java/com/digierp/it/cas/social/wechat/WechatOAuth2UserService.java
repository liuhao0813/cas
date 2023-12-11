package com.digierp.it.cas.social.wechat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class WechatOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private RestTemplate restTemplate;

    private RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
            //通过Jackson JSON processing library直接将返回值绑定到对象
//            restTemplate.getMessageConverters().add(new JacksonFromTextHtmlHttpMessageConverter());
        }
        return restTemplate;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 第一步：获取openId接口响应
        String accessToken = userRequest.getAccessToken().getTokenValue();

        Map<String, Object> additionalParameters = userRequest.getAdditionalParameters();
        String openid = (String) additionalParameters.get("openid");

        String openIdUrl = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri() + "?access_token={accessToken}&openid={openid}";
        String result = getRestTemplate().getForObject(openIdUrl, String.class, accessToken, openid);
        ObjectMapper objectMapper = new ObjectMapper();
        WeChatUserInfo weChatUserInfo;
        try {
            weChatUserInfo = objectMapper.readValue(result, WeChatUserInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return weChatUserInfo;
    }
}
