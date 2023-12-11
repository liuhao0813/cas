/**
 * 
 */
package com.digierp.it.cas.properties;

import com.digierp.it.cas.properties.BrowserProperties;
import com.digierp.it.cas.properties.ValidateCodeProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhailiang
 *
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cas.security")
public class SecurityProperties {
	
	/**
	 * 浏览器环境配置
	 */
	private BrowserProperties browser = new BrowserProperties();
	/**
	 * 验证码配置
	 */
	private ValidateCodeProperties code = new ValidateCodeProperties();
	
}

