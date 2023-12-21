package egovframework.serverConfig.security.beans;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import egovframework.serverConfig.filter.YimdoFilter;

@Configuration
public class SecurityConfiguration {
	
	@Autowired @Qualifier("taskExecutor")
	private ThreadPoolTaskExecutor taskExecutor;
	
	//패스워드 인코더
	@Bean("passwordEncoder")
	public PasswordEncoder passwordEncoder() {
		
		/*
		 *  인자값 4~31 범위를 가지며 값이 높을수록 최초 서버구동, 로그인시 걸리는 시간 증가. (미 설정시 기본값 10)
		 *  14 값은 임도서버 기준 약 1초 정도 걸림.
		 */
		return new BCryptPasswordEncoder(14);
	}
	
	@Bean
	public YimdoUserDetailsService yimdoUserDetailsService() {
		
		return new YimdoUserDetailsService();
	}
	
	@Bean
	public ProviderFactory providerFactory() {
		
		return new ProviderFactory(passwordEncoder(), yimdoUserDetailsService());
	}
	
	// 인증처리 인터페이스 구현체
	@Bean("authenticationManager")
	public AuthenticationManager authenticationManager() throws Exception {
		
		ProviderFactory providerFactory = providerFactory();
		
		List<AuthenticationProvider> authenticationProviders = List.of(
																		providerFactory.createDaoAuthenticationProvider()
																	  );
		
		ProviderManager providerManager = new ProviderManager(authenticationProviders);
		/*
		 * 인증후 자격증명(비밀번호)을 제거할지 여부.
		 * 보통 인증후에는 서버가 비밀번호를 가지고있을 필요가 없으므로 보안상 true로 설정.
		 */
		providerManager.setEraseCredentialsAfterAuthentication(true);
		
		return providerManager;
	}
	
	// 세션저장소
	@Bean("sessionRegistry")
	public SessionRegistry sessionRegistry() {
		
	    return new SessionRegistryImpl();
	}
	
	// HTTP 요청의 URL과 관련된 보안 문제 방지
    @Bean("httpFirewall")
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
    	
        StrictHttpFirewall firewall = new StrictHttpFirewall();	 // 기본적으로 URL에 사용되는 특수문자는 거부.
        firewall.setAllowUrlEncodedSlash(true);					 // URL에 '/' 허용하는지 여부
        
        return firewall;
    }
    
	// Spring Security에서 세션 관련 이벤트를 처리할 수 있게 해주는 설정, 처리내용은 SessionListener 에 구현.
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
    	
        return new HttpSessionEventPublisher();
    }
    
    @Bean("yimdoAuthenticationEntryPoint")
    public YimdoAuthenticationEntryPoint yimdoAuthenticationEntryPoint() {
    	
    	return new YimdoAuthenticationEntryPoint();
    }
    
    @Bean("aesEncrypter")
    public AesEncrypter aesEncrypter() throws UnsupportedEncodingException {
    	
    	return new AesEncrypter();
    }
    
    @Bean("yimdoFilter")
    public YimdoFilter yimdoFilter(ThreadPoolTaskExecutor taskExecutor) throws UnsupportedEncodingException {
    	
    	return new YimdoFilter(aesEncrypter(), taskExecutor);
    }
}
