package egovframework.serverConfig.security;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.SessionManagementFilter;

import egovframework.serverConfig.filter.YimdoFilter;
import egovframework.serverConfig.security.beans.YimdoAuthenticationEntryPoint;

/**
 * Spring Security 설정 클래스.
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value("${security.debug}")
    private boolean enableDebug;
    
	@Resource(name = "sessionRegistry")
	private SessionRegistry sessionRegistry;
	
	@Resource(name = "yimdoAuthenticationEntryPoint")
	private YimdoAuthenticationEntryPoint yimdoAuthenticationEntryPoint;
	
	@Resource(name = "yimdoFilter")
	private YimdoFilter yimdoFilter;
	
	// 동적 영역 설정
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		
		// 필터
		http.addFilterAfter(yimdoFilter, SessionManagementFilter.class);
		
		// 예외 처리
		http.exceptionHandling()
				.authenticationEntryPoint(yimdoAuthenticationEntryPoint);
		
		// xss
        http.headers()
        		.xssProtection()
        			.block(true);
        
        // 권한에 따른 접근가능 경로
        http.authorizeRequests()
            	.antMatchers("/general/**", "/beacon", "/test").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated();
        
        // 비로그인 사용자 인증
        http.anonymous();
        
        // csrf
        http.csrf()
            	.ignoringAntMatchers("/beacon");
        
        // 세션
        http.sessionManagement()
            	.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            	.invalidSessionUrl("/general/invalidatedSession.do")
            	.maximumSessions(1)
            	.maxSessionsPreventsLogin(true)
            	.sessionRegistry(sessionRegistry)
            	.expiredUrl("/general/expiredSession.do");
	}
	
	// 정적 영역 설정
	@Override
	public void configure(WebSecurity web) throws Exception {
		
		// 디버그 모드
		web.debug(enableDebug);
		
		// 필터링 예외 경로
	    web.ignoring()
	        	.antMatchers("/css/**", "/js/**");
	}
	
	/*
	 * 적용된 웹 보안.
	 */
	
	/*
	 * CSRF(Cross Site Request Forgery) 공격: 사용자가 자신의 의지와는 무관하게 공격자가 의도한 행위(수정, 삭제, 등록 등)를 특정 웹사이트에 요청하게 만드는 공격.
	 * 
	 * 대응:
	 * 1. 사용자의 HttpServletRequest 을 기반으로 CsrfToken 을 생성하여 CsrfTokenRepository 에 저장.
	 * 2. GET, HEAD, TRACE, OPTIONS 을 제외한 메서드에 대해 사용자의 요청안에 CsrfToken(해더에서 X-CSRF-TOKEN 또는 바디에서 _csrf 에 담긴 값) 을 추출.
	 * 3. CsrfTokenRepository 에 저장된 token 과 사용자가 보낸 token 값을 equals() 통해 일치하는지 비교.
	 * 4. 일치할경우 정상적인 다음 필터로직으로 진행되고 그러지 못한경우 InvalidCsrfTokenException 을, 만료된 토큰인 경우 MissingCsrfTokenException 을 발생.
	 */
	
	/*
	 * 세션탈취(session hijacking): 공격자가 사용자의 세션을 탈취하여 정상적인 사용자로 위장하여 접근.
	 * 
	 * 대응:
	 * 1. 로그인시 인증이 완료되었다면 해당 세션은 만료하고 새로운 세션을 발급.
	 * 2. 인증 객체에 ip, sessionId 를 저장하고 사용자 요청마다 ip, sessionId 를 대조하여 불일치 할 경우 요청 거부.
	 */
	
	/*
	 * SQL 삽입 공격(SQL injection): 공격자가 임의의 SQL 문을 주입하고 실행되게 하여 데이터베이스가 비정상적인 동작을 하도록 조작하는 행위.
	 * 
	 * 대응:
	 * 1. Prepared Statement 구문 사용을 통해 사용자가 입력한 값이 쿼리문에 변화를 주지 않도록 함.
	 */
	
	/*
	 * 서버접근
	 * 
	 * 1. 공용 리소스, 공용 경로 외에 모든 요청은 인증, 인가 된 사용자만 서버측에 수용하도록 필터를 지정.
	 * 2. '/'를 제외한 모든 특수문자는 요청경로에 사용할 수 없도록 지정.
	 */
}
