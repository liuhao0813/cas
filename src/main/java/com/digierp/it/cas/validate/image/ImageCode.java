/**
 * 
 */
package com.digierp.it.cas.validate.image;

import com.digierp.it.cas.validate.code.ValidateCode;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;



/**
 * 图片验证码
 * @author zhailiang
 *
 */
@Getter
@Setter
public class ImageCode extends ValidateCode {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6020470039852318468L;
	
	private BufferedImage image; 
	
	public ImageCode(BufferedImage image, String code, int expireIn){
		super(code, expireIn);
		this.image = image;
	}
	
	public ImageCode(BufferedImage image, String code, LocalDateTime expireTime){
		super(code, expireTime);
		this.image = image;
	}

}
