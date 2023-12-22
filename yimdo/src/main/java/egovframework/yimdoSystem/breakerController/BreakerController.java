package egovframework.yimdoSystem.breakerController;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import egovframework.common.GlobalUtil;
import egovframework.serverConfig.ServerConfig;
import egovframework.socketServer.component.SocketServerContext;
import egovframework.socketServer.component.SocketServerSender;
import egovframework.socketServer.component.SocketServerUtil;
import egovframework.web.common.vo.BreakerInfoVo;
import lombok.extern.slf4j.Slf4j;

/**
 * 차단기를 제어를 수행하는 클래스.
 */
@Component("breakerController")
@Slf4j
public class BreakerController {

	@Resource(name = "socketServerUtil")
	private SocketServerUtil socketServerUtil;
	
	@Resource(name = "socketServerSender")
	private SocketServerSender socketServerSender;

	@Resource(name = "breakerControllerMapper")
	private BreakerControllerMapper breakerControllerMapper;

	private Map<String, Boolean> processingNormalOpen = SocketServerContext.getProcessingNormalOpen();
	
	/**
	 * 차단기에서 보내는 상태값에 일치하는 차단기 정책 코드로 반환.
	 * 
	 * @param oriCode 상태코드
	 * @return 정책코드
	 */
	private String switchStatusCode(String oriCode) {
		
		String compareStatusCode = "";
		
		switch (oriCode) {
		
			case "01":
				compareStatusCode = "1001";
				break;
				
			case "02":
				compareStatusCode = "1002";
				break;
				
			case "03":
				compareStatusCode = "2001";
				break;
				
			case "04":
				compareStatusCode = "2002";
				break;
				
			default:
				compareStatusCode = "3001";
				break;
		}
		
		return compareStatusCode;
	}
	
	/**
	 * 차단기에 요청을 보내고 요청이력을 DB에 저장합니다.
	 * <p>
	 * 요청을 보낼 차단기의 정보가 담긴 BreakerInfoVo을 인자값으로 받으며,<br>
	 * 차단기에 보낼 요청 내용을 BreakerInfoVo의 setter를 통해 breakerPolicyCode, elementCode, modifier, modifyDetail
	 * 값을 변경한 후 인자값으로 전달합니다.<br>
	 * </p>
	 * <p>
	 * breakerPolicyCode 값에 따라 차단기의 정책을 수정하고 해당 차단기에 정책에 맞는 요청을 전송하고 그 이력을 DB에 저장합니다.
	 * </p>
	 * 
	 * @param breakerInfoVo DB에 저장된 차단기 정보를 가져온뒤 breakerPolicyCode, elementCode, modifier, modifyDetail 값을 수정한 Vo
	 * @return 성공시 1, 실패시 0
	 */
	public int breakerRequest(BreakerInfoVo breakerInfoVo) {
		log.debug("breakerRequest() 시작");
		log.debug("요청보내는 차단기ID: {}, 요청보내는 정책코드: {}", breakerInfoVo.getBreakerId() , breakerInfoVo.getBreakerPolicyCode());
		
		String breakerId = breakerInfoVo.getBreakerId();
		String compareStatusCode = switchStatusCode(breakerInfoVo.getBreakerStatusCode());
		
		if (!socketServerUtil.validateBreakerStatus(breakerId)) {
			
			log.debug("breakerRequest() 끝");
			return 0;
		}
		
		if (compareStatusCode.equals(breakerInfoVo.getBreakerPolicyCode())) {

			log.debug("차단기에 보내려는 요청이 이미 차단기의 상태와 일치합니다.");
			log.debug("breakerRequest() 끝");
			return 0;
		}
		
		BreakerHistoryVo breakerHistoryVo = new BreakerHistoryVo();
		
		breakerHistoryVo.setYimdoCode(breakerInfoVo.getYimdoCode());
		breakerHistoryVo.setBreakerId(breakerId);
		breakerHistoryVo.setBreakerPolicyCode(breakerInfoVo.getBreakerPolicyCode());
		breakerHistoryVo.setElementCode(breakerInfoVo.getElementCode());
		breakerHistoryVo.setModifier(breakerInfoVo.getModifier());
		breakerHistoryVo.setModifyDetail(breakerInfoVo.getModifyDetail());
		breakerHistoryVo.setDbReqDate(GlobalUtil.getNowDateTime());
		breakerHistoryVo.setSystemControl(breakerInfoVo.getSystemControl());
		
		try {
			
			breakerControllerMapper.updateBreakerPolicyDetail(breakerHistoryVo);
			breakerControllerMapper.insertBreakerHistory(breakerHistoryVo);
			
		} catch (Exception e) {

			log.error("DB 에러");
			e.printStackTrace();
			log.debug("breakerRequest() 끝");
			return 0;
		}
		
		if (compareStatusCode.equals(ServerConfig.breakerPolicyNormalOpen))
			processingNormalOpen.put(breakerId, true);
		
		boolean sendRequest = socketServerSender.sendRequestStatus(
																	breakerHistoryVo.getBreakerId(),
																	breakerHistoryVo.getBreakerPolicyCode(),
																	breakerHistoryVo.getBreakerHistoryId()
																  );
		
		log.debug("breakerRequest() 끝");
		return sendRequest ? 1 : 0;
	}
}
