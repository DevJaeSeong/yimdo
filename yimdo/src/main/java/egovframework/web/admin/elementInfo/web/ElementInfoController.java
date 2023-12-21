package egovframework.web.admin.elementInfo.web;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.api.vo.MountainWeatherVo;
import egovframework.api.vo.RiseSetInfoVo;
import egovframework.web.admin.elementInfo.service.ElementInfoService;
import egovframework.yimdoSystem.YimdoSystemController.service.YimdoSystemService;

@Controller
@RequestMapping("/admin/elementInfo")
public class ElementInfoController {
	
	@Resource(name = "elementInfoService")
	private ElementInfoService elementInfoService;
	
	@Resource(name = "yimdoSystemService")
	private YimdoSystemService yimdoSystemService;
	
	@GetMapping("/elementInfoPage.do")
	public String elementInfoPage() {
		
		return "admin/elementInfo";
	}
	
	@PostMapping("/updateElement.do")
	@ResponseBody
	public Object updateElement(@RequestBody Map<String, String> msg) {
		
		Map<String, Object> map = elementInfoService.updateElement(msg);
		String elementCode = msg.get("elementCode");
		
		if (elementCode.equals("3001") || elementCode.equals("3002") || elementCode.equals("3003")) {
			
			yimdoSystemService.controlBreakerByYimdoSystem(new MountainWeatherVo());
			
		} else if (elementCode.equals("3005") || elementCode.equals("3006")) {
			
			yimdoSystemService.controlBreakerByYimdoSystem(new RiseSetInfoVo());
		}
		
		return map;
	}
}
