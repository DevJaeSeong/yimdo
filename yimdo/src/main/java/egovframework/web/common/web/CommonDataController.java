package egovframework.web.common.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import egovframework.web.common.paging.PagingVo;
import egovframework.web.common.service.CommonDataService;
import egovframework.web.common.vo.SidoVo;
import egovframework.web.common.vo.SigunguVo;

@RestController
@RequestMapping("/common")
public class CommonDataController {
	
	@Resource(name = "commonDataService")
	private CommonDataService commonDataService;
	
	/**
	 * 시, 도 테이블을 리턴
	 */
	@GetMapping("/getSido.do")
	public Object getSido() {
		
		List<SidoVo> sidoVos = commonDataService.getSido();
		
		return sidoVos;
	}
	
	/**
	 * 시, 군, 구 테이블을 리턴
	 */
	@GetMapping("/getSigungu.do")
	public Object getSigungu(@RequestParam("sido") String sido) {
		
		List<SigunguVo> sigunguVos = commonDataService.getSigungu(sido);
		
		return sigunguVos;
	}
	
	/**
	 * 임도 테이블을 리턴
	 */
	@GetMapping("/getYimdoList.do")
	public Object getYimdoList(@ModelAttribute PagingVo pagingVo) {
		
		Map<String, Object> data = commonDataService.getYimdoData(pagingVo);
		
		return data;
	}
	
	/**
	 * 차단기 테이블을 리턴
	 */
	@GetMapping("/getBreakerList.do")
	public Object getBreakerList(@ModelAttribute PagingVo pagingVo) {
		
		Map<String, Object> data = commonDataService.getBreakerData(pagingVo);
		
		return data;
	}
	
	/**
	 * 상태변경인자 테이블 리턴
	 */
	@GetMapping("/getElementList.do")
	public Object getElemetList(@ModelAttribute PagingVo pagingVo) {
		
		Map<String, Object> data = commonDataService.getElementData(pagingVo);
		
		return data;
	}
}
