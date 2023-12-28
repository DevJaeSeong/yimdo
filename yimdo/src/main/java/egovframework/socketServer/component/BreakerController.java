package egovframework.socketServer.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import egovframework.socketServer.mapper.BreakerControllerMapper;
import egovframework.socketServer.vo.BreakerControllerVo;
import egovframework.socketServer.vo.BreakerHistoryVo;
import lombok.extern.slf4j.Slf4j;

/**
 * 차단기를 제어를 수행하는 클래스.
 */
@Component("breakerController")
@Slf4j
public class BreakerController {

	private SocketServerUtil socketServerUtil;
	private SocketServerSender socketServerSender;
	private BreakerControllerMapper breakerControllerMapper;
	
	@Autowired
	public BreakerController(SocketServerUtil socketServerUtil
							 , SocketServerSender socketServerSender
							 , BreakerControllerMapper breakerControllerMapper) {
		
		this.socketServerUtil = socketServerUtil;
		this.socketServerSender = socketServerSender;
		this.breakerControllerMapper = breakerControllerMapper;
	}

	public void breakerRequest(BreakerControllerVo breakerControllerVo) {
		log.debug("차단기 조작 요청 진행 => {}", breakerControllerVo);
		
		String breakerId = breakerControllerVo.getBreakerId();
		String breakerPolicyCode = breakerControllerVo.getBreakerPolicyCode();
		String breakerStatus;
		
		try {
			
			breakerStatus = breakerControllerMapper.selectBreakerStatusById(breakerId);
			
		} catch (Exception e) {
			
			log.debug("DB에서 차단기 상태 조회하던중 에러 발생.");
			e.printStackTrace();
			return;
		}
		
		if (!socketServerUtil.validateBreakerStatus(breakerId, breakerStatus, breakerPolicyCode)) 
			return;
		
		BreakerHistoryVo breakerHistoryVo = new BreakerHistoryVo();
		breakerHistoryVo.setBreakerId(breakerId);
		breakerHistoryVo.setBreakerPolicyCode(breakerPolicyCode);
		breakerHistoryVo.setElementCode(breakerControllerVo.getElementCode());
		breakerHistoryVo.setModifier(breakerControllerVo.getModifier());
		breakerHistoryVo.setModifyDetail(breakerControllerVo.getModifyDetail());
		breakerHistoryVo.setSystemControl(breakerControllerVo.getSystemControl());
		
		try {
			
			breakerControllerMapper.updateBreakerPolicyDetail(breakerHistoryVo);
			breakerControllerMapper.insertBreakerHistory(breakerHistoryVo);
			
		} catch (Exception e) {

			log.error("DB 에러");
			e.printStackTrace();
		}
		
		socketServerSender.sendRequestToBreaker(breakerId, breakerPolicyCode, breakerHistoryVo.getBreakerHistoryId());
	}
}
