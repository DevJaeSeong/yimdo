package egovframework.yimdoSystem.YimdoSystemController.service;

import java.net.Socket;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.api.vo.BreakerControlElement;
import egovframework.api.vo.MountainWeatherVo;
import egovframework.api.vo.RiseSetInfoVo;
import egovframework.socketServer.component.SocketServerContext;
import egovframework.web.common.service.impl.CommonDataMapper;
import egovframework.web.common.vo.BreakerInfoVo;
import egovframework.web.common.vo.ModifyElementVo;
import egovframework.yimdoSystem.apiDataFetcher.ApiDataFetcher;
import egovframework.yimdoSystem.breakerController.BreakerController;
import lombok.extern.slf4j.Slf4j;

@Service("yimdoSystemService")
@Slf4j
public class YimdoSystemService extends AbstractConditionChecker {
	
	@Resource(name = "apiDataFetcher")
	private ApiDataFetcher apiDataFetcher;
	
	@Resource(name = "commonDataMapper")
	private CommonDataMapper commonDataMapper;
	
	@Resource(name = "breakerController")
	private BreakerController breakerController;
	
	private Map<String, Socket> socketMap = SocketServerContext.getSocketMap();
	
	/**
	 * DB에 저장된 차단기들을 원하는 인자에 의해 상태가 변경되도록 수행.
	 * 
	 * @param breakerControlElement 차단기 제어인자로 사용되는 Vo
	 */
	public void controlBreakerByYimdoSystem(BreakerControlElement breakerControlElement) {
		
		if (socketMap.size() <= 0) {
			
			log.trace("연결된 소켓이 없습니다.");
			return;
		}
		
		List<BreakerInfoVo> breakerInfoVos;
		List<ModifyElementVo> modifyElementVos;
		
		try {
			
			breakerInfoVos = commonDataMapper.getAllBreakerList();
			modifyElementVos = commonDataMapper.getUsedModifyElementList();
			
		} catch (Exception e) {
			
			log.error("차단기 리스트를 불러오는데 실패.");
			e.printStackTrace();
			return;
		}
		
		for (BreakerInfoVo breakerInfoVo : breakerInfoVos) {
			
			if (!isSystemControlBreaker(breakerInfoVo)) continue;
			
			doControlByElement(breakerInfoVo, modifyElementVos, breakerControlElement);
		}
	}

	/**
	 * 해당 차단기가 시스템이 제어해도 되는상태인지 판별.
	 * 
	 * @param breakerInfoVo 차단기
	 * @return 가능한 상태인경우 true<br>
	 * 		   강제 개방, 강제 차단 상태인경우 false
	 */
	private boolean isSystemControlBreaker(BreakerInfoVo breakerInfoVo) {
		log.trace("isSystemControlBreaker() 시작");
		
		boolean result = true;
		String breakerPolicyCode = breakerInfoVo.getBreakerPolicyCode();
		
		if (!breakerInfoVo.getSystemControl().equals("y")) {
			
			if (breakerPolicyCode.equals("2001") || breakerPolicyCode.equals("2002")) {
				
				log.debug("\"{}\" 차단기는 관리자에 의해 강제 개방 / 강제 차단 중 입니다.", breakerInfoVo.getBreakerId());
				result = false; 
			}
		}
		
		log.trace("isSystemControlBreaker() 끝");
		return result;
	}
	
	/**
	 * 인자값에 따라 차단기 상태를 변경.
	 * 
	 * @param breakerInfoVo
	 * @param modifyElementVos
	 * @param breakerControlElement
	 */
	private void doControlByElement(BreakerInfoVo breakerInfoVo, List<ModifyElementVo> modifyElementVos, BreakerControlElement breakerControlElement) {
		
		if (breakerControlElement instanceof MountainWeatherVo)
			controlBreakerByMountainWeather(breakerInfoVo, modifyElementVos);
			
		else if (breakerControlElement instanceof RiseSetInfoVo)
			controlBreakerBySunsetSunrise(breakerInfoVo, modifyElementVos);
		
		// 비교인자가 추가될 경우 여기에 추가.
	}
	
	/**
	 * 산악기상정보 강수량, 습도, 풍속에 의한 제어
	 * 
	 * @param breakerInfoVo
	 * @param modifyElementVos
	 * @return
	 */
	private int controlBreakerByMountainWeather(BreakerInfoVo breakerInfoVo, List<ModifyElementVo> modifyElementVos) {
		log.trace("controlBreakerByMountainWeather() 시작");
		
		MountainWeatherVo mountainWeatherVo;
		
		try {
			
			mountainWeatherVo = apiDataFetcher.getMountainWeatherData("4913");
			log.trace("산 이름: {}", mountainWeatherVo.getM_obsname());
			
		} catch (Exception e) {
			
			log.error("인자값 가져오는데 실패.");
			e.printStackTrace();
			log.trace("controlBreakerByMountainWeather() 끝");
			return 0;
		}
		
		String baseRainFall = "";
		String baseWindSpeed = "";
		String baseHumidity = "";
		String compareRainFall = mountainWeatherVo.getM_cprn();
		String compareWindSpeed = mountainWeatherVo.getM_ws10m();
		String compareHumidity = mountainWeatherVo.getM_hm2m();
		
		for (ModifyElementVo modifyElementVo : modifyElementVos) {
			
			switch (modifyElementVo.getElementCode()) {
			
				case "3001":
					baseRainFall = modifyElementVo.getElementValue();
					break;
					
				case "3002":
					baseWindSpeed = modifyElementVo.getElementValue();
					break;
					
				case "3003":
					baseHumidity = modifyElementVo.getElementValue();
					break;

				default:
					break;
			}
			
		}
		
		boolean isAppropriateCondition = true;
		StringBuilder sb = new StringBuilder("인자값(");
		
		if (!super.isAppropriateRainfall(baseRainFall, compareRainFall)) {
			
			sb.append("강수량, ");
			isAppropriateCondition = false;
		}
		
		if (!super.isAppropriateWindspeed(baseWindSpeed, compareWindSpeed)) {
			
			sb.append("풍속, ");
			isAppropriateCondition = false;
		}
		
		if (!super.isAppropriateHumidity(baseHumidity, compareHumidity)) {
			
			sb.append("습도, ");
			isAppropriateCondition = false;
		}
		
		sb.setLength(sb.length() - 2);
		sb.append(")에 의한 강제차단.");
		
		breakerInfoVo.setElementCode("0001");
		breakerInfoVo.setModifier("시스템");
		
		if (!isAppropriateCondition) {
			
			breakerInfoVo.setBreakerPolicyCode("2002");
			breakerInfoVo.setModifyDetail(sb.toString());
			
		} else {
			
			breakerInfoVo.setBreakerPolicyCode("1002");
			breakerInfoVo.setModifyDetail("시스템에 의한 강제차단 해제.");
		}
		
		int result = breakerController.breakerRequest(breakerInfoVo);
		
		log.trace("controlBreakerByMountainWeather() 끝");
		return result;
	}
	
	/**
	 * 일몰, 일출 시간에 따른 제어
	 * 
	 * @param breakerInfoVo
	 * @param modifyElementVos
	 * @return
	 */
	private int controlBreakerBySunsetSunrise(BreakerInfoVo breakerInfoVo, List<ModifyElementVo> modifyElementVos) {
		log.trace("controlBreakerBySunsetSunrise() 시작");
		
		RiseSetInfoVo riseSetInfoVo;
		
		try {
			
			riseSetInfoVo = apiDataFetcher.getSunsetSunriseData();
			
		} catch (Exception e) {
			
			log.error("인자값 가져오는데 실패.");
			e.printStackTrace();
			log.trace("controlBreakerBySunsetSunrise() 끝");
			return 0;
		}
		
		String baseSunrise = "";
		String baseSunset = "";
		String sunriseTime = riseSetInfoVo.getR_sunrise();
		String sunsetTime = riseSetInfoVo.getR_sunset();
		
		for (ModifyElementVo modifyElementVo : modifyElementVos) {
			
			switch (modifyElementVo.getElementCode()) {
			
				case "3005":
					baseSunrise = modifyElementVo.getElementValue();
					break;
					
				case "3006":
					baseSunset = modifyElementVo.getElementValue();
					break;

				default:
					break;
			}
		}
		
		boolean isAppropriateCondition = true;
		StringBuilder sb = new StringBuilder("인자값(");
		
		if (!super.isAfterSunrise(baseSunrise, sunriseTime)) {
			
			sb.append("일출, ");
			isAppropriateCondition = false;
		}
		
		if (!super.isBeforeSunset(baseSunset, sunsetTime)) {
			
			sb.append("일몰, ");
			isAppropriateCondition = false;
		}
		
		sb.setLength(sb.length() - 2);
		sb.append(")에 의한 강제차단.");
		
		breakerInfoVo.setElementCode("0001");
		breakerInfoVo.setModifier("시스템");
		
		if (!isAppropriateCondition) {
			
			breakerInfoVo.setBreakerPolicyCode("2002");
			breakerInfoVo.setModifyDetail(sb.toString());
			
		} else {
			
			breakerInfoVo.setBreakerPolicyCode("1002");
			breakerInfoVo.setModifyDetail("시스템에 의한 강제차단 해제.");
		}
		
		int result = breakerController.breakerRequest(breakerInfoVo);
		
		log.trace("controlBreakerBySunsetSunrise() 끝");
		return result;
	}
}
