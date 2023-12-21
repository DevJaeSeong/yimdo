package egovframework.web.general;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.common.CookieUtil;
import egovframework.serverConfig.security.vo.UserVo;
import egovframework.web.member.main.service.MemberService;
import egovframework.web.member.main.vo.MemberVoForCreate;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/general")
@Slf4j
public class HomeController {
	
	@Resource(name = "memberService")
	private MemberService memberService;
	
	/**
	 * 사용자 메인화면
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/home.do")
	public String home() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		//memberService.showAuthentication(authentication);
		
		if (authentication == null) {
			
			log.debug("사용자 인증이 되어있지 않습니다.");
			return "redirect:/general/login.do";
			
		} else if (authentication instanceof AnonymousAuthenticationToken) {
			
			log.debug("로그인하지 않은 사용자입니다.");
			return "redirect:/general/login.do";
		}
		
		String role = ((List<GrantedAuthority>) authentication.getAuthorities()).get(0).getAuthority();
		String nextPage = "";
		
		switch (role) {
		
			case "ROLE_ADMIN":
				log.debug("이 계정의 권한은 '관리자' 이므로 관리자 페이지로 이동합니다.");
				nextPage = "redirect:/admin/main.do";
				break;
				
			case "ROLE_USER":
				log.debug("이 계정의 권한은 '사용자' 이므로 사용자 페이지로 이동합니다.");
				nextPage = "redirect:/member/main.do";
				break;
	
			default:
				log.debug("알 수 없는 권한 입니다.");
				nextPage = "redirect:/general/login.do";
				break;
		}
		
		return nextPage;
	}
	
	@GetMapping("/invalidatedSession.do")
	public String invalidatedSession(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		log.debug("sessionId: \"{}\" 이 세션은 유효하지 않은 세션입니다. 로그인페이지로 이동합니다.", session.getId());
		
		CookieUtil.deleteIdentifyToken(request, response);
		
		return "redirect:/general/login.do";
	}
	
	@GetMapping("/expiredSession.do")
	public String expiredSession(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		log.debug("sessionId: \"{}\" 이 세션은 만료된 세션입니다. 로그인페이지로 이동합니다.", session.getId());
		
		CookieUtil.deleteIdentifyToken(request, response);
		
		return "redirect:/general/login.do";
	}
	/**
	 * 로그인 화면
	 */
	@GetMapping("/login.do")
	public String login() {
		
		return "general/login";
	}
	
	@PostMapping("/loginConfirm.do")
	@ResponseBody
	public Object loginConfirm(@Valid @RequestBody UserVo userVo, HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> map = memberService.loginConfirm(userVo, request, response);
		
		return map;
	}
	
	@GetMapping("/logout.do")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		
		memberService.logout(request, response);
		
		return "redirect:/general/home.do";
	}
	
	/**
	 * 회원가입 화면
	 */
	@GetMapping("/createAccountPage.do")
	public String createAccountPage() {
		
		return "general/createAccountPage";
	}
	
	@PostMapping("/createAccount.do")
	@ResponseBody
	public Object createAccount(@RequestBody MemberVoForCreate memberVoForCreate) {
		
		Map<String, Object> map = memberService.createAccount(memberVoForCreate);
		
		return map;
	}
}
