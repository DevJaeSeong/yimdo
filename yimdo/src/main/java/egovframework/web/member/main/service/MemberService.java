package egovframework.web.member.main.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import egovframework.serverConfig.security.vo.UserVo;
import egovframework.web.member.main.vo.MemberVoForCreate;

public interface MemberService {

	public Map<String, Object> loginConfirm(UserVo userVo, HttpServletRequest request, HttpServletResponse response);
	public void showAuthentication(Authentication authentication);
	public void logout(HttpServletRequest request, HttpServletResponse response);
	public Map<String, Object> createAccount(MemberVoForCreate memberVoForCreate);
}
