package egovframework.web.member.main.web;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.web.member.main.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController {
	
	@Resource(name = "memberService")
	private MemberService memberService;
	
	@GetMapping("/main.do")
	public String main(Model model) {
		
		model.addAttribute("userDetailVo", SecurityContextHolder.getContext().getAuthentication().getDetails());
		
		return "member/home";
	}
}
