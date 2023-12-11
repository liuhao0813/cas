/**
 * 
 */
package com.digierp.it.cas.validate.code;


import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

/**
 * 校验码相关安全配置
 * 
 * @author zhailiang
 *
 */
@Component("ValidateCodeSecurityConfig")
@Setter(onMethod_ = {@Autowired})
public class ValidateCodeSecurityConfig extends AbstractHttpConfigurer<ValidateCodeSecurityConfig, HttpSecurity> {

	private ValidateCodeFilter validateCodeFilter;
	
	@Override
	public void configure(HttpSecurity http) {
		http.addFilterBefore(validateCodeFilter, AbstractPreAuthenticatedProcessingFilter.class);
	}
	
}
