package com.digierp.it.cas.config;

import com.digierp.it.cas.filter.TenantFilter;
import com.digierp.it.cas.social.qq.CompositeOAuth2AccessTokenResponseClient;
import com.digierp.it.cas.social.qq.CompositeOAuth2UserService;
import com.digierp.it.cas.social.qq.QQOAuth2AccessTokenResponseClient;
import com.digierp.it.cas.social.qq.QQOAuth2UserService;
import com.digierp.it.cas.social.wechat.WeChatOAuth2AccessTokenResponseClient;
import com.digierp.it.cas.social.wechat.WechatOAuth2UserService;
import com.digierp.it.cas.validate.code.ValidateCodeSecurityConfig;
import com.digierp.it.cas.validate.sms.SmsCodeAuthenticationSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WebSecurityConfig {

    public static final String QQRegistrationId = "qq";
    public static final String WeChatRegistrationId = "wechat";

    public static final String LoginPagePath = "/login/oauth2";

    private final ValidateCodeSecurityConfig validateCodeSecurityConfig;

    private final SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .with(validateCodeSecurityConfig, (dsl) -> {})
                .with(smsCodeAuthenticationSecurityConfig, dsl->{})
                .addFilterBefore(new TenantFilter(), AuthorizationFilter.class)
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/", "/favicon.ico", "/code/*").permitAll()
                                .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin.loginPage("/cas-login.html").loginProcessingUrl("/login/image").permitAll())
                .oauth2Login(oauth2Login -> oauth2Login
                        .authorizationEndpoint(authorization->
                                authorization.authorizationRequestResolver(
                                        authorizationRequestResolver(clientRegistrationRepository)
                                ))
                        // 使用CompositeOAuth2AccessTokenResponseClient
                        .tokenEndpoint(tokenEndpointConfig ->
                                tokenEndpointConfig
                                        .accessTokenResponseClient(this.accessTokenResponseClient()))
                        // .customUserType(WeChatUserInfo.class, QQRegistrationId)
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oauth2UserService()))
                        // 可选，要保证与redirect-uri-template匹配
                        .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig.baseUri("/register/social/*"))
                        .loginPage(LoginPagePath))
                .logout(Customizer.withDefaults());
        return httpSecurity.build();
    }

    /**
     * OAuth2微信认证的时候，添加对应的appid参数，因为微信的认证参数不是clientId
     * @param clientRegistrationRepository
     * @return
     */
    private OAuth2AuthorizationRequestResolver authorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository){
        DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
        ClientRegistration wechat = clientRegistrationRepository.findByRegistrationId("wechat");
        authorizationRequestResolver.setAuthorizationRequestCustomizer(customizer->customizer.additionalParameters(params -> params.put("appid", wechat.getClientId())));
        return authorizationRequestResolver;
    }

    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        CompositeOAuth2AccessTokenResponseClient client = new CompositeOAuth2AccessTokenResponseClient();
        // 加入QQ自定义QQOAuth2AccessTokenResponseClient 和Wexin自定义
        Map<String, OAuth2AccessTokenResponseClient> oAuth2AccessTokenResponseClients = client.getOAuth2AccessTokenResponseClients();
        oAuth2AccessTokenResponseClients.put(QQRegistrationId, new QQOAuth2AccessTokenResponseClient());
        oAuth2AccessTokenResponseClients.put(WeChatRegistrationId, new WeChatOAuth2AccessTokenResponseClient());
        return client;
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        CompositeOAuth2UserService service = new CompositeOAuth2UserService();
        // 加入QQ自定义QQOAuth2UserService和加入wexin自定义WeChatOAuth2UserService
        Map<String, OAuth2UserService> userServices = service.getUserServices();
        userServices.put(QQRegistrationId, new QQOAuth2UserService());
        userServices.put(WeChatRegistrationId, new WechatOAuth2UserService());
        return service;
    }
}
