package com.icss.sys.base.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 应用级异常,可控（业务人员误操作产生的异常，如用户不存在、密码错误、用户名已经存在等）
 */
public class TokenException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	public TokenException() {
	}

	public TokenException(Throwable cause) {
		super(cause);
	}

	public TokenException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenException(String message) {
		super(message);
	}
}
