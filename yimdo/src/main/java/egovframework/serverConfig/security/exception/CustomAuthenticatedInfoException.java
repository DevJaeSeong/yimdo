package egovframework.serverConfig.security.exception;

import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticatedInfoException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public CustomAuthenticatedInfoException(String msg) {
		super(msg);
	}
}
