package egovframework.serverConfig.security.beans;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ProviderFactory {

	private PasswordEncoder passwordEncoder;
	private UserDetailsService userDetailsService;
	
	/**
	 * {@link AuthenticationProvider} 의 구현체를 팩토리 생성.
	 * 
	 * @param passwordEncoder 암호화에 사용되는 구현체
	 * @param userDetailsService 사용자 정보가 담긴 {@link User} 객체를 가져오는 구현체
	 */
	public ProviderFactory(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
		
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
	}
	
	/**
	 * {@link AuthenticationProvider} 의 구현체, 사용자의 아이디, 패스워드를 이용하여 인증을 진행하는<br>
	 * {@link DaoAuthenticationProvider} 를 반환. 해당 객체는 {@link YimdoUserDetailsService} 를 사용.
	 * 
	 * @return new {@link DaoAuthenticationProvider}
	 */
	public DaoAuthenticationProvider createDaoAuthenticationProvider() {
		
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		/*
		 * 인증과정에서 principal 값을 문자열로 강제 변환할지 여부.
		 * 아래 boolean값에 따라 principal 값이 정해짐.
		 * true: UserDetails.getUsername()
		 * false: UserDetails
		 */
		daoAuthenticationProvider.setForcePrincipalAsString(false);
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		/*
		 * 인증과정에서 UserNotFoundException 발생시 해당 예외를 숨길지 여부.
		 * 입력한 ID가 DB에 저장되어있는지 유무를 알리고 싶지 않다면 true로 설정.
		 */
		daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
		
		return daoAuthenticationProvider;
	}
}
