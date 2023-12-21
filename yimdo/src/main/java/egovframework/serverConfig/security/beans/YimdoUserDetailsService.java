package egovframework.serverConfig.security.beans;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import egovframework.serverConfig.security.mapper.SecurityMapper;
import egovframework.serverConfig.security.vo.UserVo;
import egovframework.serverConfig.security.vo.YimdoUser;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link DaoAuthenticationProvider}가 인증을 위해 사용자 인증정보를 가져오는데 사용되는 {@link UserDetailsService}의 구현체
 */
@Slf4j
public class YimdoUserDetailsService implements UserDetailsService {

	@Resource(name = "securityMapper")
	private SecurityMapper securityMapper;
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		
		UserVo userVo = null;
		
		try {
			
			userVo = securityMapper.getUserByIdForLogin(userId);
			
		} catch (Exception e) {
			
			log.error("DB 에러 발생");
			e.printStackTrace();
		}
		
		if (userVo == null) { throw new UsernameNotFoundException("\"" + userId + "\" 를 DB에서 조회하지 못함"); }
		
		UserDetails userDetails = new YimdoUser(
												 userVo.getUserId(), 
												 userVo.getPassword(), 
												 userVo.isEnabled(), 
												 userVo.isAccountNonExpired(), 
												 userVo.isCredentialsNonExpired(), 
												 userVo.isAccountNonLocked(), 
												 getAuthorities(userVo.getAuthorityId())
											   );
		
		return userDetails;
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(int AuthorityId) {

		List<GrantedAuthority> authorities;
		
		switch (AuthorityId) {
		
			case 1:
				authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
				break;
	
			case 2:
				authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
				break;
				
			default:
				authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
				break;
		}
		
		return authorities;
	}
}
