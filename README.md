# 多种方式登录

### 用户名密码方式登录
#### 使用UserDetailsService接口，使用动态用户进行登录
#### 设置对应的登录成功或是登录失败处理器
#### 设置自定义登录页面

### 用户名密码+图片认证码方式登录

### 记住我功能

### 手机短信登录
> 注意配置对应的AuthenticationManager和SecurityContextRepository详情见下面代码
```java
    @Override
	public void configure(HttpSecurity http) throws Exception {
		
		SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
		smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(casAuthenticationSuccessHandler);
		smsCodeAuthenticationFilter.setAuthenticationFailureHandler(casAuthenticationFailureHandler);
		SecurityContextRepository securityContextRepository = http.getSharedObject(SecurityContextRepository.class);
		smsCodeAuthenticationFilter.setSecurityContextRepository(securityContextRepository);

//		String key = UUID.randomUUID().toString();
//		smsCodeAuthenticationFilter.setRememberMeServices(new PersistentTokenBasedRememberMeServices(key, userDetailsService, persistentTokenRepository));
		
		SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
		smsCodeAuthenticationProvider.setUserDetailsService(userDetailsService);
		
		http.authenticationProvider(smsCodeAuthenticationProvider)
			.addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}
```
### GitHub登录
> 默认框架就支持，只需要配置对应的client-id client-secret redirect-uri
```properties
spring.security.oauth2.client.registration.github.client-id=584417c59c093e8a1b4b
spring.security.oauth2.client.registration.github.client-secret=3b1d78660b9a382bc41013b7a4ee58f67d3776d6
spring.security.oauth2.client.registration.github.redirect-uri={baseUrl}/register/social/{registrationId}
```
### QQ登录
### 微信登录
> 微信登录注意对应的调整扫码URL，需要配置对应的appid，框架默认支持的是client-id
> 微信开放平台回调填上对应的域名即可，不需要协议，不要对应的路径
```java
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
```


