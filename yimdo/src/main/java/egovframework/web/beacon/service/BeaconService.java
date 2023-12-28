package egovframework.web.beacon.service;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.serverConfig.ServerConfig;
import egovframework.serverConfig.security.mapper.SecurityMapper;
import egovframework.serverConfig.security.vo.UserDetailVo;
import egovframework.socketServer.component.BreakerController;
import egovframework.socketServer.component.SocketServerContext;
import egovframework.socketServer.vo.BreakerControllerVo;
import egovframework.web.common.service.impl.CommonDataMapper;
import egovframework.web.common.vo.BreakerInfoVo;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BeaconService {

	private Set<String> carDetectionBreakers = SocketServerContext.getCarDetectionBreakers();
	private SecurityMapper securityMapper;
	private CommonDataMapper commonDataMapper;
	private BreakerController breakerController;
	
	@Autowired
	public BeaconService(BreakerController breakerController,
						 CommonDataMapper commonDataMapper,
						 SecurityMapper securityMapper) {
		
		this.breakerController = breakerController;
		this.commonDataMapper = commonDataMapper;
		this.securityMapper = securityMapper;
	}

	public void acceptBeaconSignal(Map<String, String> reciveData) {
		
		String userId = reciveData.get("USER_ID");
		String breakerId = reciveData.get("BREAKER_ID");
		
		if (userId == null || breakerId == null) {
			
			log.debug("사용자ID 또는 차단기ID가 null값");
			log.debug("userId: {}, breakerId: {}", userId, breakerId);
		}
		
		log.debug("\"{}\" 차단기 차량감지중인지 확인 => {}", breakerId, carDetectionBreakers);
		
		if (!carDetectionBreakers.contains(breakerId)) {
			
			log.debug("\"{}\" 차단기에 차량이 감지되어있지 않음", breakerId);
			return;
		}
		
		UserDetailVo userDetailVo;
		BreakerInfoVo breakerInfoVo;
		
		try {
			
			userDetailVo = securityMapper.getUserDetailVoById(userId);
			breakerInfoVo = commonDataMapper.getBreakerById(breakerId);
			
		} catch (Exception e) {
			
			log.error("DB 에러");
			e.printStackTrace();
			return;
		}
		
		BreakerControllerVo breakerControllerVo = new BreakerControllerVo(breakerId
																		  , ServerConfig.BREAKER_POLICY_NORMAL_OPEN
																		  , "사용자"
																		  , ServerConfig.BEACON_ELEMENT_CODE
																		  , "비콘통신을 이용한 사용자의 개방요청");
		
		breakerController.breakerRequest(breakerControllerVo);
	}
}
