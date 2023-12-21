package egovframework.web.admin.imdoAccessHistory.web;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.web.admin.imdoAccessHistory.service.ImdoAccessHistoryService;
import egovframework.web.common.paging.PagingVo;

@Controller
@RequestMapping("/admin/imdoAccessHistory")
public class ImdoAccessHistoryController {
	
	@Resource(name = "imdoAccessHistoryService")
	private ImdoAccessHistoryService imdoAccessHistoryService;
	
	@GetMapping("/imdoAccessHistoryPage.do")
	public String imdoAccessHistoryPage() {
		
		return "admin/imdoAccessHistory";
	}
	
	@GetMapping("/getAccessHistory.do")
	@ResponseBody
	public Object getAccessHistory(@ModelAttribute PagingVo pagingVo) {
		
		Map<String, Object> data = imdoAccessHistoryService.getData(pagingVo);
		
		return data;
	}
}
