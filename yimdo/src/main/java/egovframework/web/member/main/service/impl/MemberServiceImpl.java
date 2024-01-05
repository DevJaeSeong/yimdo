package egovframework.web.member.main.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import egovframework.common.CookieUtil;
import egovframework.serverConfig.ServerConfig;
import egovframework.serverConfig.security.beans.YimdoAuthenticater;
import egovframework.serverConfig.security.vo.UserVo;
import egovframework.web.member.main.service.MemberService;
import egovframework.web.member.main.vo.MemberVoForCreate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("memberService")
@AllArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
	
	private MemberMapper memberMapper;
	private PasswordEncoder encoder;
	private YimdoAuthenticater yimdoAuthenticater;
	
	/**
	 * @param data 외부로부터 받은 id, pw 문자열을 담은 Map
	 * @return new Map(key: "result", value: "success"(성공시) / "fail"(실패시)
	 */
	@Override
	public Map<String, Object> loginConfirm(UserVo userVo, HttpServletRequest request, HttpServletResponse response) {
		log.debug("loginConfirm() 시작");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			yimdoAuthenticater.authenticateUser(userVo.getUserId(), userVo.getPassword(), request, response);
			setSessionMaxInactiveIntervalForAdmin(request);
			map.put("result", "success");
			
		} catch (Exception e) {
			
			log.error("로그인 인증실패: {}", e.getMessage());
			map.put("result", "fail");
		}
		
		log.debug("loginConfirm() 끝");
		return map;
	}
	
	/**
	 * 로그인한 계정의 권한이 관리자인 경우 세션 유지기간을 확장.
	 * 
	 * @param request
	 */
	private void setSessionMaxInactiveIntervalForAdmin(HttpServletRequest request) {
		
		@SuppressWarnings("unchecked")
		String role = ((List<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities()).get(0).getAuthority();
		
		if ("ROLE_ADMIN".equals(role)) { 
			
			request.getSession().setMaxInactiveInterval(ServerConfig.SESSION_MAX_INACTIVE_INTERVAL_FOR_ADMIN); 
		}
	}
	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		log.debug("logout() 시작");
		
		request.getSession().invalidate();
		request.getSession(true);
		
		CookieUtil.deleteCookie(ServerConfig.IDENTIFY_TOKEN_NAME, request, response);
		
		log.debug("logout() 끝");
	}
	
	/**
	 * 인증정보 내용을 콘솔창에 출력합니다.
	 * @param authentication 해당 사용자의 인증정보
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void showAuthentication(Authentication authentication) {
		
		if (authentication != null) {
			
			log.debug("principal: {}", authentication.getPrincipal());
			log.debug("details: {}", authentication.getDetails());
			log.debug("name: {}", authentication.getName());
			log.debug("autority: {}", ((List<GrantedAuthority>) authentication.getAuthorities()).get(0).getAuthority());
			
		} else {
			
			log.debug("authentication: {}", authentication);
		}
	}

	@Override
	public Map<String, Object> createAccount(MemberVoForCreate memberVoForCreate) {
		log.debug("createAccount() 시작");
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			
			if (memberMapper.getCountById(memberVoForCreate) > 0) {
				
				map.put("result", "already_exists");
				log.debug("이미 존제하는 계정입니다.");
				log.debug("createAccount() 끝");
				return map;
			}
			
			memberVoForCreate.setPassword(encoder.encode(memberVoForCreate.getPassword()));
			memberMapper.createAccount(memberVoForCreate);
			map.put("result", "success");
			
		} catch (Exception e) {

			map.put("result", "fail");
			log.error("계정정보 입력도중 에러발생");
			e.printStackTrace();
		}
		
		log.debug("createAccount() 끝");
		return map;
	}
}
