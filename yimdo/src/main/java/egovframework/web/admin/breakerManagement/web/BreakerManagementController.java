package egovframework.web.admin.breakerManagement.web;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.web.admin.breakerManagement.service.BreakerManagementService;
import egovframework.web.admin.breakerManagement.vo.BreakerManagementVo;

@Controller
@RequestMapping("/admin/breakerManagement")
public class BreakerManagementController {
	
	@Resource(name = "breakerManagementService")
	private BreakerManagementService breakerManagementService;
	
	@GetMapping("/breakerManagementPage.do")
	public String breakerManagementPage(Model model) {
		
		model.addAttribute("memberVo", SecurityContextHolder.getContext().getAuthentication().getDetails());
		
		return "admin/breakerManagement";
	}
	
	@PostMapping("/updateBreakerStatus.do")
	@ResponseBody
	public Map<String, Object> updateBreakerStatus(@RequestBody BreakerManagementVo breakerManagementVo) {
		
		Map<String, Object> map = breakerManagementService.updateBreakerStatus(breakerManagementVo);
		
		return map;
	}
}
