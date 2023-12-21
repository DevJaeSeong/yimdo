package egovframework.web.admin.managementHistory.web;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.web.admin.managementHistory.service.BreakerHistoryService;
import egovframework.web.common.paging.PagingVo;

@Controller
@RequestMapping("/admin/managementHistory")
public class BreakerHistoryController {
	
	@Resource(name = "breakerHistoryService")
	private BreakerHistoryService breakerHistoryService;
	
	@GetMapping("/breakerHistoryPage.do")
	public String breakerHistoryPage() {
		
		return "admin/breakerHistory";
	}
	
	/**
	 * 차단기 상태변경 이력 테이블 리턴
	 */
	@GetMapping("/getBreakerHistoryList.do")
	@ResponseBody
	public Object getBreakerHistoryList(@ModelAttribute PagingVo pagingVo) {
		
		Map<String, Object> data = breakerHistoryService.getData(pagingVo);
		
		return data;
	}
}
