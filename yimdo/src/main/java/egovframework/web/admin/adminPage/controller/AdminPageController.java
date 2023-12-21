package egovframework.web.admin.adminPage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminPageController {
	
	/**
	 * 관리자 메인
	 */
	@GetMapping("/main.do")
	public String main() {
		
		return "admin/main";
	}
}
