/**
 * 
 */
package com.digierp.it.cas.support;

/**
 * 简单响应的封装类
 * 
 * @author zhailiang
 *
 */
public class SimpleResponse {
	
	public SimpleResponse(Object content){
		this.content = content;
	}
	
	private Object content;

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
}
