package egovframework.serverConfig.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import egovframework.serverConfig.ServerConfig;
import egovframework.serverConfig.security.beans.AesEncrypter;
import egovframework.serverConfig.security.exception.DifferentAuthenticatedInfoException;
import egovframework.serverConfig.security.vo.YimdoUser;
import lombok.extern.slf4j.Slf4j;

/**
 * 커스텀 필터.
 */
@Slf4j
public class YimdoFilter extends OncePerRequestFilter {
	
	private final AesEncrypter aesEncrypter;
	private final Executor executor;
	
	public YimdoFilter(AesEncrypter aesEncrypter, Executor executor) {
		
		super();
		
		this.aesEncrypter = aesEncrypter;
		this.executor = executor;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
        
		request = new YimdoServletRequestWrapper(request, executor);
		
		printRequest((YimdoServletRequestWrapper) request);
		
		validateAuthentication(request);
		
		filterChain.doFilter(request, response);
	}
	
	private void validateAuthentication(HttpServletRequest request) throws AuthenticationException {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication instanceof AnonymousAuthenticationToken) {
			
			log.debug("비로그인 사용자");
			return;
		}
	    
		YimdoUser yimdoUser = (YimdoUser) authentication.getPrincipal();
		String authenticatedIp = yimdoUser.getAuthenticatedIp();
		String requestIp = request.getRemoteAddr();
		
		/*
		 * AuthenticationException 및 AccessDeniedException 를 발생시켜야 ExceptionTranslationFilter 에서 처리가능.
		 */
		
		if (!authenticatedIp.equals(requestIp)) {
			
			throw new DifferentAuthenticatedInfoException("요청ip와 인증된ip가 다름 (요청ip: " + requestIp + ", 인증된ip: " + authenticatedIp + ")");
		}
		
		String authenticatedSessionId = yimdoUser.getAuthenticatedSessionId();
		String requestSessionId = request.getRequestedSessionId();
		
		if (!authenticatedSessionId.equals(requestSessionId)) {
			
			throw new DifferentAuthenticatedInfoException("요청sessionId와 인증된sessionId가 다름 (요청sessionId: " + requestSessionId + ", 인증된sessionId: " + authenticatedSessionId + ")");
		}
		
		String identifyTokenValue = yimdoUser.getIdentifyTokenValue();
		String requestIdentifyTokenValue = "";
		Cookie[] cookies = request.getCookies();
		
		for (Cookie cookie : cookies) {
			
			if (ServerConfig.IDENTIFY_TOKEN_NAME.equals(cookie.getName())) {
				
				try {
					
					requestIdentifyTokenValue = aesEncrypter.decrypt(cookie.getValue());
					
				} catch (UnsupportedEncodingException | GeneralSecurityException e) {
					
					e.printStackTrace();
					throw new DifferentAuthenticatedInfoException("복호화 도중 문제 발생: " + e.getMessage());
				}
			}
		}
		
		if (!identifyTokenValue.equals(requestIdentifyTokenValue)) {
			
			throw new DifferentAuthenticatedInfoException("요청identifyTokenValue와 저장된identifyTokenValue가 다름 (요청identifyTokenValue: " + requestIdentifyTokenValue + ", 저장된identifyTokenValue: " + identifyTokenValue + ")");
		}
	}

	/**
	 * 서버에 접근하는 사용자의 정보를 콘솔에 출력.
	 */
	private void printRequest(YimdoServletRequestWrapper request) {
		
		String requestURI = request.getRequestURI();
		String remoteAddr = request.getRemoteAddr();
		String sessionId = request.getRequestedSessionId();
		String method = request.getMethod();
		String requestBody = readRequest(request);
		String principal = "";
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			
			principal = ((YimdoUser) authentication.getPrincipal()).toString();
			
		} else if (authentication instanceof AnonymousAuthenticationToken) {
			
			principal = (String) authentication.getPrincipal();
		}
		
		log.debug("사용자: [\"{}\", \"{}\", {}]", remoteAddr, sessionId, principal);
		log.debug("요청내용: [({}) \"{}\", \"{}\"]", method, requestURI, requestBody);
	}

	private String readRequest(YimdoServletRequestWrapper request) {
		
		String method = request.getMethod();
		String requestData = readParameters(request);
		
		if (!"GET".equals(method)) {
			
			if (request.getContentType().toLowerCase().contains("json")) {
				
				requestData = new String(request.getRequestData());
			} 
		}
		
		return requestData;
	}
	
    private String readParameters(HttpServletRequest request) {
    	
        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, Object> parameterMap = new LinkedHashMap<String, Object>();
        
        while (parameterNames.hasMoreElements()) {
        	
            String parameterName = parameterNames.nextElement();
            String parameterValue = request.getParameter(parameterName);
            
            parameterMap.put(parameterName, parameterValue);
        }
		
    	return parameterMap.toString();
    }
}
