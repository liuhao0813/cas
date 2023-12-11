/**
 * 
 */
package com.digierp.it.cas.validate.code.image;

import javax.imageio.ImageIO;

import com.digierp.it.cas.validate.code.impl.AbstractValidateCodeProcessor;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;


/**
 * 图片验证码处理器
 * 
 * @author zhailiang
 *
 */
@Component("imageValidateCodeProcessor")
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

	/**
	 * 发送图形验证码，将其写到响应中
	 */
	@Override
	protected void send(ServletWebRequest request, ImageCode imageCode) throws Exception {
		HttpServletResponse response = request.getResponse();
		response.setHeader("Content-Type", "image/png");
		ImageIO.write(imageCode.getImage(), "png", response.getOutputStream());
	}

}
