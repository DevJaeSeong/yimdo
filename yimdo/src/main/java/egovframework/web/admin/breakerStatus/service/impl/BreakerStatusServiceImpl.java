package egovframework.web.admin.breakerStatus.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.socketServer.component.BreakerController;
import egovframework.socketServer.vo.BreakerControllerVo;
import egovframework.web.admin.breakerStatus.service.BreakerStatusService;
import egovframework.web.admin.breakerStatus.vo.BreakerManagementVo;
import egovframework.web.common.service.impl.CommonDataMapper;
import lombok.extern.slf4j.Slf4j;

@Service("breakerStatusService")
@Slf4j
public class BreakerStatusServiceImpl implements BreakerStatusService {
	
	@Resource(name = "commonDataMapper")
	private CommonDataMapper commonDataMapper;
	
	@Resource(name = "breakerStatusMapper")
	private BreakerStatusMapper breakerStatusMapper;
	
	@Resource(name = "breakerController")
	private BreakerController breakerController;

	@Override
	public Map<String, Integer> getBreakerListEachStatusCount(Map<String, String> msgMap) {
		log.trace("getBreakerListEachStatusCount() 시작");

		Map<String, Integer> statusCounts = null;
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				statusCounts = breakerStatusMapper.getBreakerListEachStatusCount(msgMap);
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}
		
		log.trace("getBreakerListEachStatusCount() 끝");
		return statusCounts;
	}

	/**
	 * 외부로부터 입력받은 값을 바탕으로 차단기의 정책을 수정하고 정책 수정한 기록을 DB에 저장하고
	 * 차단기에 정책에 맞는 개방, 차단 신호를 보냄. 
	 * @param breakerManagementVo 변경할 차단기ID 리스트, 상태변경인자, 차단기정책, 변경상세내용, 변경자
	 * @return 각 breakerId를 key값으로 해당 차단기에 대한 로직이 정상적으로 수행됬다면 "success", 실패했다면 "fail"
	 */
	@Override
	public void updateBreakerStatus(BreakerManagementVo breakerManagementVo) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<String> selectedBreakers = breakerManagementVo.getSelectedBreakers();
		String modifier = breakerManagementVo.getModifier();
		String modifyDetail = breakerManagementVo.getModifyDetail();
		String elementCode = breakerManagementVo.getSelectedElement();
		String policyCode = breakerManagementVo.getSelectedPolicy();
		String systemControl = breakerManagementVo.getSystemControl();
		
		for (String breakerId : selectedBreakers) {
			
    		BreakerControllerVo breakerControllerVo = new BreakerControllerVo(breakerId, 
    																		  policyCode, 
    																		  modifier, 
    																		  elementCode, 
																			  modifyDetail);
    		
    		breakerControllerVo.setSystemControl(systemControl);
    		
    		breakerController.breakerRequest(breakerControllerVo);
			map.put(breakerId, "success");
		}
	}
}
