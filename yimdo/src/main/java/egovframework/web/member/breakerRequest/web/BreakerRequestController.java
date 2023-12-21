package egovframework.web.member.breakerRequest.web;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.web.member.breakerRequest.service.BreakerRequestService;

@Controller
@RequestMapping("/user")
public class BreakerRequestController {
	
	@Resource(name = "breakerRequestService")
	private BreakerRequestService breakerRequestService;
	
	/**
	 * 차단기 개방/차단 요청을 보내고 성공여부를 리턴.
	 * @param msgMap 요청할 차단기 breakerId 와 요청내용 request 을 받아옴
	 * @return map 요청 처리 결과를 리턴함
	 */
	@PostMapping("/clientRequestNormalOpen.do")
	@ResponseBody
	public Object clientRequestNormalOpen(@RequestBody Map<String, String> msgMap) {
    	
		Map<String, Object> map = breakerRequestService.clientRequestNormalOpen(msgMap);
		
		return map;
	}
}
