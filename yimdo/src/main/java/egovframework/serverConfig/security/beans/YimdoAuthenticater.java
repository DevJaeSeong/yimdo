package egovframework.serverConfig.security.beans;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import egovframework.common.CookieUtil;
import egovframework.serverConfig.ServerConfig;
import egovframework.serverConfig.security.mapper.SecurityMapper;
import egovframework.serverConfig.security.vo.YimdoUser;
import lombok.extern.slf4j.Slf4j;

/**
 * 사용자 인증, 권한 인가를 수행하는 클래스.
 */
@Component("yimdoAuthenticater")
@Slf4j
public class YimdoAuthenticater {
	
	@Resource(name = "securityMapper")
	private SecurityMapper securityMapper;
	
	@Resource(name = "authenticationManager")
	private AuthenticationManager authenticationManager;
	
	@Resource(name = "aesEncrypter")
	private AesEncrypter aesEncrypter;
	
	/** 로그인 되어있는 계정모음 (중복로그인 방지에 사용) */
	public static Map<String, HttpSession> ActiveAccounts = new ConcurrentHashMap<String, HttpSession>();
	
	/**
	 * 사용자가 입력한 아이디, 패스워드를 가지고 사용자 인증을 진행<p>
	 * 진행 순서:
	 * <p>
	 * 1. 인증 진행. (실패시 Exception 발생)</br>
	 * 2. 기존 세션을 만료 하고 새로운 세션 생성. (세션 고정 보호)</br>
	 * 3. 단일 로그인 적용. (중복 로그인 방지)</br>
	 * 4. 인증 정보를 SecurityContext에 저장.</br>
	 * 5. 사용자 식별용 쿠키 발행.</br>
	 * </p>
	 * 인증이 정상적으로 처리되었다면 해당 코드를 통해 로그인한 계정의 MemberVo, 부여된 권한, 사용자 아이디를 획득할 수 있습니다.
	 * <pre>
	 * <code>
	 * String role = ((List&ltGrantedAuthority&gt) SecurityContextHolder.getContext().getAuthentication().getAuthorities()).get(0).getAuthority();
	 * UserDetailVo userDetailVo = (UserDetailVo) SecurityContextHolder.getContext().getAuthentication().getDetails();
	 * String userId = SecurityContextHolder.getContext().getAuthentication().getName();
	 * </code>
	 * </pre>
	 * 
	 * @param id 사용자가 입력한 아이디
	 * @param pw 사용자가 입력한 패스워드
	 * @param request 사용자의 {@link HttpServletRequest}
	 * @param response 사용자의 {@link HttpServletResponse}
	 * @exception AuthenticationException 인증 실패
	 * @exception Exception
	 */
	public void authenticateUser(String id, String pw, HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("authenticateUser() 시작");
		
		// 사용자 인증에 필요한 토큰 발행.
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(id, pw);
		token.setDetails(securityMapper.getUserDetailVoById(id));
		
		// 토큰을 바탕으로 인증 진행. (실패시 AuthenticationException 발생)
		Authentication authentication = authenticationManager.authenticate(token);
		
		// 세션 교체. (세션 ID 고정 방지)
		changeNewSession(request, authentication);
		
		// 단일 로그인 적용. (중복 로그인 방지)
		enforceSingleLogin(id, request);
		
		// 식별토큰 발급
		createIdentifyToken(request, response, authentication);
		
		// 인증 정보를 SecurityContext에 저장.
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		log.debug("authenticateUser() 끝");
	}

	/**
	 * 로그인 되어있지 않은 계정인 경우 해당 세션을 ActiveAccounts에 추가.<br>
	 * 로그인 되어있는 계정인 경우 기존 세션을 만료하고 로그인 하려하는 세션으로 교체.
	 * 
	 * @param userId 로그인 하려는 계정 ID
	 * @param request 로그인 하려는 사용자의 {@link HttpServletRequest}
	 */
	private void enforceSingleLogin(String userId, HttpServletRequest request) {
		
		// 로그인이 되어있지 않은 계정인 경우.
		if (!ActiveAccounts.containsKey(userId)) {
			
			ActiveAccounts.put(userId, request.getSession());
			log.debug("\"{}\" 로그인", userId);
			return;
		}
		
		// 이미 로그인이 되어있는 경우.
		HttpSession oldAccountSession = (HttpSession) ActiveAccounts.get(userId);
		
		if (oldAccountSession != null) {
			
			log.debug("해당 계정으로 로그인 되어있던 세션을 만료합니다. sessionId: {}", oldAccountSession.getId());
			
			try 				{ oldAccountSession.invalidate(); }
			catch (Exception e) { log.error("이미 만료된 세션입니다."); }
		}
		
		ActiveAccounts.put(userId, request.getSession());
		log.debug("\"{}\" 로그인 (중복 로그인 방지 적용)", userId);
	}
	

	/**
	 * 세션을 교체하고 {@link YimdoUser}에 ip와 sessionId를 등록
	 * 
	 * @param request
	 * @param authentication 사용자 인증객체
	 */
	private void changeNewSession(HttpServletRequest request, Authentication authentication) {
		
		HttpSession session = request.getSession();
		
		if (!session.isNew()) {
			
			session.invalidate();
			session = request.getSession();
		}
		
		session.setMaxInactiveInterval(ServerConfig.SESSION_MAX_INACTIVE_INTERVAL);
		
		// 사용자 정보에 IP 등록.
		YimdoUser yimdoUser = (YimdoUser) authentication.getPrincipal();
		yimdoUser.setAuthenticatedIp(request.getRemoteAddr());
		yimdoUser.setAuthenticatedSessionId(session.getId());
	}
	
	private void createIdentifyToken(HttpServletRequest request, HttpServletResponse response, Authentication authentication) 
			throws NoSuchAlgorithmException, UnsupportedEncodingException, GeneralSecurityException, UnknownHostException {
		
		String randomString = aesEncrypter.generateRandomString(16);
		
		YimdoUser yimdoUser = (YimdoUser) authentication.getPrincipal();
		yimdoUser.setIdentifyTokenValue(randomString);
		
		CookieUtil.sendCookie(ServerConfig.IDENTIFY_TOKEN_NAME, aesEncrypter.encrypt(randomString), request, response);
	}
}
