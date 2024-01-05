package egovframework.serverConfig.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.SessionManagementFilter;

import egovframework.serverConfig.filter.YimdoFilter;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security 설정 클래스.
 */
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {
	
	
    private boolean enableDebug;
	private SessionRegistry sessionRegistry;
	private YimdoFilter yimdoFilter;
	
	public WebSecurityConfig(@Value("${security.debug}") boolean enableDebug
									, SessionRegistry sessionRegistry
									, YimdoFilter yimdoFilter) {
		
		this.enableDebug = enableDebug;
		this.sessionRegistry = sessionRegistry;
		this.yimdoFilter = yimdoFilter;
	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		
		return (web) -> web
				.debug(enableDebug)
				.ignoring().requestMatchers("/css/**", "/js/**");
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.addFilterAfter(yimdoFilter, SessionManagementFilter.class);
		
		http.exceptionHandling((exceptionHandlingCustomizer) -> exceptionHandlingCustomizer
				
			.authenticationEntryPoint((request, response, authException) -> {
				
				log.debug("인증 에러 발생: {}", authException.getMessage());
				response.sendRedirect("/general/logout.do");
			})
			.accessDeniedHandler((request, response, accessDeniedException) -> {
				
				log.debug("인증 에러 발생: {}", accessDeniedException.getMessage());
				response.sendRedirect("/general/logout.do");
			})
		);
		
		http.authorizeHttpRequests((requests) -> requests
				
			.requestMatchers("/general/**", "/beacon", "/test").permitAll()
			.requestMatchers("/admin/**").hasRole("ADMIN")
			.requestMatchers("/user/**").hasAnyRole("ADMIN", "USER")
			.anyRequest().authenticated()
		);
		
		http.csrf((csrfCustomizer) -> csrfCustomizer
				
			.ignoringRequestMatchers("/beacon")
		);

        http.sessionManagement((sessionManagementCustomizer) -> sessionManagementCustomizer
        		
        	.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        	.invalidSessionUrl("/general/invalidatedSession.do")
        	.maximumSessions(1)
        	.maxSessionsPreventsLogin(true)
        	.sessionRegistry(sessionRegistry)
        	.expiredUrl("/general/expiredSession.do")
        );
		
		return http.build();
	}
}
