package egovframework.web.admin.breakerStatus.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.web.admin.breakerStatus.service.BreakerStatusService;
import egovframework.web.admin.breakerStatus.vo.BreakerManagementVo;

@Controller
@RequestMapping("/admin/breakerStatus")
public class BreakerStatusController {
	
	@Resource(name = "breakerStatusService")
	private BreakerStatusService breakerStatusService;
	
	@GetMapping("/breakerStatusPage.do")
	public String breakerStatusPage() {
		
		return "admin/breakerStatus";
	}
	
	/**
	 * 선택한 임도의 차단기들의 각각상태 갯수를 리턴
	 */
	@GetMapping("/getBreakerListEachStatusCount.do")
	@ResponseBody
	public Object getBreakerListEachStatusCount(@RequestParam Map<String, String> msgMap) {
		
		Map<String, Integer> statusCounts = breakerStatusService.getBreakerListEachStatusCount(msgMap);
		
		return statusCounts;
	}
	
	@PostMapping("/updateBreakerStatus.do")
	@ResponseBody
	public Map<String, Object> updateBreakerStatus(@RequestBody BreakerManagementVo breakerManagementVo) {
		
		Map<String, Object> result = new HashMap<>();
		
		try {
			
			breakerStatusService.updateBreakerStatus(breakerManagementVo);
			result.put("result", "success");
			
		} catch (Exception e) {
			
			result.put("result", "fail");
			e.printStackTrace();
		}
		
		return result;
	}
}
