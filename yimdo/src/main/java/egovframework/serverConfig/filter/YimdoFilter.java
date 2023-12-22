package egovframework.serverConfig.filter;

import java.io.IOException;
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

import egovframework.common.CookieUtil;
import egovframework.serverConfig.ServerConfig;
import egovframework.serverConfig.security.beans.AesEncrypter;
import egovframework.serverConfig.security.exception.CustomAuthenticatedInfoException;
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
		
		this.aesEncrypter = aesEncrypter;
		this.executor = executor;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
        
		request = new YimdoServletRequestWrapper(request, executor);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		printRequest((YimdoServletRequestWrapper) request, authentication);
		
		if (!(authentication instanceof AnonymousAuthenticationToken))
			validateAuthentication(request, (YimdoUser) authentication.getPrincipal());
		
		filterChain.doFilter(request, response);
	}
	
	private void validateAuthentication(HttpServletRequest request, YimdoUser yimdoUser) throws AuthenticationException {
		
		String authenticatedIp = yimdoUser.getAuthenticatedIp();
		String requestIp = request.getRemoteAddr();
		
		String authenticatedSessionId = yimdoUser.getAuthenticatedSessionId();
		String requestSessionId = request.getRequestedSessionId();
		
		Cookie cookie = CookieUtil.getCookie(ServerConfig.IDENTIFY_TOKEN_NAME, request);
		String identifyTokenValue = yimdoUser.getIdentifyTokenValue();
		String requestIdentifyTokenValue = "";
		
		// IP 검사
		if (!authenticatedIp.equals(requestIp))
			throw new CustomAuthenticatedInfoException("요청ip와 인증된ip가 다름 (요청ip: " + requestIp + ", 인증된ip: " + authenticatedIp + ")");
		
		// sessionId 검사
		if (!authenticatedSessionId.equals(requestSessionId))
			throw new CustomAuthenticatedInfoException("요청sessionId와 인증된sessionId가 다름 (요청sessionId: " + requestSessionId + ", 인증된sessionId: " + authenticatedSessionId + ")");
		
		// 식별토큰 검사
		if (cookie == null)
			throw new CustomAuthenticatedInfoException("식별토큰을 가져오지 못했습니다.");
		
		try { requestIdentifyTokenValue = aesEncrypter.decrypt(cookie.getValue()); } 
		catch (Exception e) {
			
			e.printStackTrace();
			throw new CustomAuthenticatedInfoException("복호화 도중 문제 발생: " + e.getMessage());
		}
		
		if (!identifyTokenValue.equals(requestIdentifyTokenValue))
			throw new CustomAuthenticatedInfoException("요청identifyTokenValue와 저장된identifyTokenValue가 다름 (요청identifyTokenValue: " + requestIdentifyTokenValue + ", 저장된identifyTokenValue: " + identifyTokenValue + ")");
	}

	/**
	 * 서버에 접근하는 사용자의 정보를 콘솔에 출력.
	 */
	private void printRequest(YimdoServletRequestWrapper request, Authentication authentication) {
		
		String requestURI = request.getRequestURI();
		String remoteAddr = request.getRemoteAddr();
		String sessionId = request.getRequestedSessionId();
		String method = request.getMethod();
		String requestBody = readRequest(request);
		String principal = "";
		
		if (authentication instanceof UsernamePasswordAuthenticationToken)
			principal = ((YimdoUser) authentication.getPrincipal()).toString();
		
		else if (authentication instanceof AnonymousAuthenticationToken)
			principal = (String) authentication.getPrincipal();
		
		log.debug("사용자: [\"{}\", \"{}\", {}], 요청내용: [({}) \"{}\", \"{}\"]", remoteAddr, sessionId, principal, method, requestURI, requestBody);
	}

	private String readRequest(YimdoServletRequestWrapper request) {
		
		String requestData = readParameters(request);
		
		if (!"GET".equals(request.getMethod())) {
			
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
