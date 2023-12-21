package egovframework.serverConfig.listener;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import egovframework.serverConfig.ServerConfig;
import egovframework.serverConfig.security.beans.YimdoAuthenticater;
import lombok.extern.slf4j.Slf4j;

/**
 * 각종 이벤트 리스너를 구현한 클래스.
 */
@WebListener
@Slf4j
public class YimdoWebListener implements HttpSessionListener, ServletContextListener {
	
	private SessionRegistry sessionRegistry;
	
	/**
	 * 세션 생성시 콜백함수.
	 */
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		log.trace("sessionCreated() 시작");
		
		HttpSession session = se.getSession();
		String sessionId = session.getId();
		
		session.setMaxInactiveInterval(ServerConfig.SESSION_MAX_INACTIVE_INTERVAL);
		
		sessionRegistry.registerNewSession(sessionId, session);
		
		log.debug("created session: {}", sessionId);
		log.trace("sessionCreated() 끝");
	}

	/**
	 * 세션 만료시 세션에 저장된 모든 속성들(MemberVo, 인증정보 등)을 제거합니다.
	 * @param se 만료되는 세션의 이벤트 객체
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		log.trace("sessionDestroyed() 시작");
		
		HttpSession session = se.getSession();
		
		removeIdFromActiveAccounts();
		
		// 세션 만료후 세션목록에서 제거.
		SessionInformation sessionInformation = sessionRegistry.getSessionInformation(session.getId());
		
		if (sessionInformation != null) {
			
			sessionInformation.expireNow();
			//sessionRegistry.removeSessionInformation(session.getId());
		}
	    
		// 세션 안에 저장되어있는 속성 삭제.
		Enumeration<String> attributeNames = session.getAttributeNames();
		
	    while (attributeNames.hasMoreElements()) {
	    	
	        String sessionAttributeName = attributeNames.nextElement();
	        Object sessionAttributeValue = session.getAttribute(sessionAttributeName);
	        session.removeAttribute(sessionAttributeName);
	        log.trace("sessionAttributeName: {}, sessionAttributeValue: {}", sessionAttributeName, sessionAttributeValue);
	    }
	    
	    // SecurityContextHolder 안에 저장된 인증정보 제거.
	    SecurityContextHolder.clearContext();
	    
	    log.trace("세션에 정보가 남아있는지 확인: {}", session.getAttributeNames().hasMoreElements());
	    log.trace("SecurityContextHolder에 SecurityContext 가 남아있는지 확인: {}", SecurityContextHolder.getContext());
	    
	    if (sessionInformation != null)
	    	log.debug("destroyed session: {}, session expired: {}", session.getId(), sessionInformation.isExpired());
	    else
	    	log.debug("destroyed session: {}, session expired: null", session.getId());
	    
	    log.trace("sessionDestroyed() 끝");
	}

	/**
	 * 로그아웃시 ActiveAccounts에서 해당 계정 정보 제거.
	 */
	private void removeIdFromActiveAccounts() {
		
		String userId = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication != null) {
			
			userId = authentication.getName();
		}
		
	    // ActiveAccounts 저장된 계정 정보 삭제.
	    if (userId != null && YimdoAuthenticater.ActiveAccounts.containsKey(userId)) {
	    	
	    	YimdoAuthenticater.ActiveAccounts.remove(userId);
	    	log.debug("\"{}\" 로그아웃", userId);
	    }
	}
	
	/**
	 * 서버 시작시 로직
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		sessionRegistry = (SessionRegistry) webApplicationContext.getBean("sessionRegistry");
		
		expireAllSession();
	}

	/**
	 * 서버 종료시 로직
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
		expireAllSession();
	}
	
	/*
	private int getAllAccessedSessionCount() {
		
		int result = 0;
		List<Object> principals = sessionRegistry.getAllPrincipals();
		
		for (Object principal : principals) {
			
			List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(principal, false);
			result += sessionInformations.size();
		}
		
		return result;
	}
	*/
	
	/**
	 * {@link sessionRegistry}에 저장된 모든 {@link SessionInformation}을 expireNow();
	 */
	private void expireAllSession() {
		
		List<Object> principals = sessionRegistry.getAllPrincipals();
		
		for (Object principal : principals) {
			
			List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(principal, false);
			
			for (SessionInformation sessionInformation : sessionInformations) {
				
				sessionInformation.expireNow();
			}
		}
	}
}
