package egovframework.web.member.accessYimdo.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.web.common.vo.YimdoInfoVo;
import egovframework.web.member.accessYimdo.service.AccessYimdoService;
import egovframework.web.member.accessYimdo.vo.PurposeEntryVo;
import egovframework.web.member.accessYimdo.vo.RequestEntryYimdoVo;

@Controller
@RequestMapping("/member")
public class AccessYimdoController {
	
	@Resource(name = "accessYimdoService")
	private AccessYimdoService accessYimdoService;
	
	/**
	 * 임도 출입 신청서 작성 페이지
	 */
	@GetMapping("/accessYimdo.do")
	public String accessYimdo(HttpSession session, Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		List<PurposeEntryVo> purposeEntryVos = accessYimdoService.getPurposeEntryList();
		List<YimdoInfoVo> yimdoInfoVos = accessYimdoService.getYimdoInfoList();
		
		model.addAttribute("userDetailVo", authentication.getDetails());
		model.addAttribute("purposeEntryVos", purposeEntryVos);
		model.addAttribute("yimdoInfoVos", yimdoInfoVos);
		
		return "member/accessYimdo";
	}
	
	@PostMapping("/accessYimdoConfirm.do")
	@ResponseBody
	public Object accessYimdoConfirm(@RequestBody RequestEntryYimdoVo requestEntryYimdoVo) {
		
		Map<String, Object> map = accessYimdoService.insertRequestEntryYimdo(requestEntryYimdoVo);
		
		return map;
	}
}
