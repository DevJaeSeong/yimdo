package egovframework.socketServer.component;

import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import egovframework.serverConfig.ServerConfig;
import egovframework.socketServer.mapper.SocketServerMapper;
import egovframework.web.admin.weatherData.vo.WeatherDataVo;
import egovframework.web.common.vo.BreakerInfoVo;
import egovframework.yimdoSystem.breakerController.BreakerController;
import lombok.extern.slf4j.Slf4j;

/**
 * 소켓통신을 통해 받은 데이터를 처리하는 클래스.
 */
@Component("socketServerReceiver")
@Slf4j
public class SocketServerReceiver {

	@Resource(name = "socketServerMapper")
	private SocketServerMapper socketServerMapper;
	
	@Resource(name = "socketServerUtil")
	private SocketServerUtil socketServerUtil;
	
	@Resource(name = "breakerController")
	private BreakerController breakerController;
	
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	
	private Map<String, Socket> socketMap = SocketServerContext.getSocketMap();
	private Set<String> runningBreakers = SocketServerContext.getRunningBreakers();
	private Map<String, Boolean> processingNormalOpen = SocketServerContext.getProcessingNormalOpen();
	private Set<String> carDetectionBreakers = SocketServerContext.getCarDetectionBreakers();
	
	/**
     * 받은 byte[] 데이터를 파싱하여 Command ID값에 따라 분기를 나눠 데이터 처리.
     * @param data 받은 데이터
     */
    protected void processIncomingData(byte[] data) {
    	log.trace("processIncomingData() 시작");
		
    	byte commandID = (byte) data[4];
    	byte[] payload = new byte[data.length - ServerConfig.HEADER_LENGTH];
    	byte[] deviceId = {(byte) data[1], (byte) data[2]};
    	String deviceIdtoString = socketServerUtil.deviceIdToString(deviceId);
    	
    	try {
			
    		for (int i = 0; i < (data.length - ServerConfig.HEADER_LENGTH); i++) {
    			
    			payload[i] = (byte) data[ServerConfig.HEADER_LENGTH + i];
    		}
    		
		} catch (Exception e) {

			log.warn("data 조회 범위를 초과함");
			e.printStackTrace();
		}
    	
    	if (commandID == 0x14 || commandID == 0x15) {
    		
        	if (runningBreakers.contains(deviceIdtoString))
        		runningBreakers.remove(deviceIdtoString);
    	}
    	
    	/*
    	log.debug("0x23:{}, 0x21:{}, 0x14:{}, 0x15:{}, 0x27:{}, 0x30:{}"
    			, commandID == 0x23, commandID == 0x21, commandID == 0x14, commandID == 0x15, commandID == 0x27, commandID == 0x30);
    	*/
    	
    	switch (commandID) {
    	
			case 0x23: // tcp 연결요청
				connectionResponse(deviceIdtoString);
				break;
			
			case 0x21: // 차단기 상태보고
				presenceReport(deviceIdtoString, payload);
				break;
			
			case 0x14: // 정상요청 응답
				normalBreakerResponse(deviceIdtoString, payload);
				break;
				
			case 0x15: // 강제요청 응답
				emergencyBreakerResponse(deviceIdtoString, payload);
				break;
				
			case 0x27: // 차단기 차량 감지
				carDetectionEventReport(deviceIdtoString, payload);
				break;
				
			case 0x28: // 차단기 사람 감지
				log.debug("사람 감지");
				break;
				
			case 0x29: // 스마트폰이 비컨 감지시 서버에 인증요청
				log.debug("비콘 감지");
				break;
				
			case 0x30: // 차단기에서 수집된 기상정보
				weatherReport(deviceIdtoString, payload);
				break;
				
			default:
				log.warn("정의되지않은 commandID");
				break;
		}
    	
    	log.trace("processIncomingData() 끝");
	}

	/**
     * 차단기에서 Connection Request 요청이 들어왔을때 Connection Response 으로 응답.
     * @param deviceIdtoString 문자열로된 deviceId
     */
	public void connectionResponse(String deviceIdtoString) {
		log.debug("connectionResponse() 시작");
		
		try {
			
			Socket socket = (Socket) socketMap.get(deviceIdtoString);
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("deviceId", deviceIdtoString);
			map.put("breakerIp", socket.getInetAddress().getHostAddress());
			
			socketServerMapper.updateBreakerIp(map);
			
			byte[] payload = {(byte) 0xAA};
			byte[] sendData = socketServerUtil.makeByteData(deviceIdtoString, (byte) 0x13, (byte) 0x01, payload);
			
			OutputStream out = socket.getOutputStream();
			out.write(sendData);
			out.flush();
			
			socketServerUtil.printByteData(sendData, "연결 성공 응답");
			
		} catch (Exception e) {

			log.error("connectionResponse() 처리중 에러발생");
			e.printStackTrace();
		}
    	
    	log.debug("connectionResponse() 끝");
	}
	
    /**
     * 차단기에서 주기적으로 보내는 상태값을 DB에 저장.
     * @param breakerId 문자열로된 deviceId
     * @param payload 받은 데이터의 payload 부분
     */
	public void presenceReport(String breakerId, byte[] payload) {
		log.trace("presenceReport() 시작");
		
    	String breakerOpened;
    	
    	switch (payload[0] & 0xff) {
    	
			case 0x01:
				breakerOpened = "01";
				break;
				
			case 0x02:
				breakerOpened = "02";
				break;
				
			case 0x03:
				breakerOpened = "03";
				break;
				
			case 0x04:
				breakerOpened = "04";
				break;
				
			case 0x05:
				breakerOpened = "05";
				break;
	
			default:
				log.warn("정의되지 않은 값");
				breakerOpened = "05";
				break;
		}
    	
    	log.trace("breakerOpened: {}", breakerOpened);
    	
    	String breakerPolicyCode = "";
    	
    	try {
    		
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("breakerId", breakerId);
    		map.put("breakerStatus", breakerOpened);
    		
    		socketServerMapper.insertPresenceReport(map);
    		socketServerMapper.updateBreakerStatus(map);
    		breakerPolicyCode = socketServerMapper.selectBreakerPolicy(breakerId);
    		
		} catch (Exception e) {
			
			log.error("DB통신 에러");
			e.printStackTrace();
			log.trace("presenceReport() 끝");
			return;
		}
    	
		if (!runningBreakers.contains(breakerId))
			doMatchBreakerStatus(breakerPolicyCode, breakerOpened, breakerId);
		else
			log.trace("정상개방 후 정상차단 대기중 입니다.");

    	log.trace("presenceReport() 끝");
	}
	
	private void doMatchBreakerStatus(String breakerPolicyCode, String breakerOpened, String breakerId) {
		log.trace("breakerPolicyCode: {}", breakerPolicyCode);
		
		String compareCode = "";
		
		if 		(ServerConfig.breakerPolicyNormalOpen.equals(breakerPolicyCode)) 		compareCode = "01";
		else if (ServerConfig.breakerPolicyNormalClose.equals(breakerPolicyCode)) 		compareCode = "02";
		else if (ServerConfig.breakerPolicyEmergencyOpen.equals(breakerPolicyCode)) 	compareCode = "03";
		else if (ServerConfig.breakerPolicyEmergencyClose.equals(breakerPolicyCode)) 	compareCode = "04";
		else 																	 		compareCode = "05";
		
    	if (breakerOpened.equals(compareCode)) return;
    	else log.warn("차단기 상태와 정책이 불일치하여 일치하도록 다시 차단기에 요청합니다.");
    	
    	if (breakerOpened.equals("05")) {
    		
    		log.debug("차단기 상태가 에러이므로 요청을 전송하지 않습니다.");
    		return;
    	}
    	
    	try {
    		
    		BreakerInfoVo breakerInfoVo = socketServerMapper.selectBreakerById(breakerId);
    		breakerInfoVo.setBreakerPolicyCode(breakerPolicyCode);
    		breakerInfoVo.setElementCode("0001");
    		breakerInfoVo.setModifier("시스템");
    		breakerInfoVo.setModifyDetail("차단기 정책과 상태 불일치로 인한 재요청");
    		
    		if (breakerController.breakerRequest(breakerInfoVo) > 0)
    			log.debug("요청 전송");
    		else
    			log.warn("요청 전송 실패");
    		
    	} catch (Exception e) {
    		
    		log.error("DB에러");
    		e.printStackTrace();
    	}
	}
	
	/**
	 * 차단기에서 Normal Breaker Response 으로 보낸 요청수신시각, 요청완료시각을 DB에 저장.
	 * @param deviceIdtoString 차단기ID
	 * @param payload 수신된 데이터
	 */
	public void normalBreakerResponse(String deviceIdtoString, byte[] payload) {
		log.debug("normalBreakerResponse() 시작");
		
		if ((payload[0] & 0xff) == 0x55) {
			
			log.warn("차단기가 동작에 실패");
			log.debug("normalBreakerResponse() 끝");
			return;
		}
		
		String dateTime = "";
		byte[] breakerHistoryIdByteArray = new byte[4];
		
		try {
			
			for (int i = 0; i < payload.length - 4; i++) {
				
				dateTime += (char) payload[i + 1];
			}
			
			Map<String, Object> map = socketServerUtil.dateTimeSplit(dateTime);
			
			for (int i = 0; i < 4; i++) {
				
				breakerHistoryIdByteArray[i] = payload[payload.length - (4 - i)];
			}
			
			int breakerHistoryId = socketServerUtil.byteArrayToInt(breakerHistoryIdByteArray);
			log.debug("breakerHistoryId: {}", breakerHistoryId);
			
			map.put("breakerHistoryId", breakerHistoryId);
			
			if (processingNormalOpen.containsKey(deviceIdtoString))
				processForNormalOpen(deviceIdtoString);
			else
				socketServerMapper.updateBreakerHistory(map);
			
		} catch (Exception e) {

			log.error("normalBreakerResponse() 처리중 에러발생");
			e.printStackTrace();
		}
		
		log.debug("normalBreakerResponse() 끝");
	}
	
	/**
	 * 정상개방 요청인 경우 별도 작업 수행.
	 * 
	 * @param deviceIdtoString 차단기ID
	 * @throws Exception
	 */
	private void processForNormalOpen(String deviceIdtoString) throws Exception {
		
		boolean isProcessingNormalOpen = processingNormalOpen.get(deviceIdtoString);
		
		if (isProcessingNormalOpen) {
			
			processingNormalOpen.put(deviceIdtoString, false);
			
		} else {
			
			Map<String, String> updatePolicyMap = new HashMap<String, String>();
			updatePolicyMap.put("breakerId", deviceIdtoString);
			updatePolicyMap.put("breakerPolicyCode", ServerConfig.breakerPolicyNormalClose);
			
			socketServerMapper.updateBreakerPolicy(updatePolicyMap);
			
			processingNormalOpen.remove(deviceIdtoString);
		}
	}
	
	/**
	 * 차단기에서 Emergency Breaker Response 으로 보낸 요청수신시각, 요청완료시각을 DB에 저장.
	 * @param deviceIdtoString 차단기ID
	 * @param payload 수신된 데이터
	 */
	public void emergencyBreakerResponse(String deviceIdtoString, byte[] payload) {
		log.debug("emergencyBreakerResponse() 시작");
		
		if ((payload[0] & 0xff) == 0x55) {
			
			log.warn("차단기가 동작에 실패");
			log.debug("normalBreakerResponse() 끝");
			return;
		}
		
		String dateTime = "";
		byte[] breakerHistoryIdByteArray = new byte[4];
		
		try {
			
			for (int i = 0; i < payload.length - 4; i++) {
				
				dateTime += (char) payload[i + 1];
			}
			
			Map<String, Object> map = socketServerUtil.dateTimeSplit(dateTime);
			
			for (int i = 0; i < 4; i++) {
				
				breakerHistoryIdByteArray[i] = payload[payload.length - (4 - i)];
			}
			
			int breakerHistoryId = socketServerUtil.byteArrayToInt(breakerHistoryIdByteArray);
			log.debug("breakerHistoryId: {}", breakerHistoryId);
			
			map.put("breakerHistoryId", breakerHistoryId);
			
			socketServerMapper.updateBreakerHistory(map);
			
		} catch (Exception e) {

			log.error("normalBreakerResponse() 처리중 에러발생");
			e.printStackTrace();
		}
		
		log.debug("emergencyBreakerResponse() 끝");
	}
	
	/**
	 * 차단기에서 전달한 기상정보를 DB에 저장.
	 * 
	 * @param deviceIdtoString 차단기ID
	 * @param payload 바이트 배열로 이뤄진 데이터
	 */
	private void weatherReport(String deviceIdtoString, byte[] payload) {

		List<byte[]> segments;
		
		try {
			
			segments = socketServerUtil.splitByteArray(payload, (byte) 0x2C);
			
		} catch (Exception e) {
			
			log.error("바이트 배열을 나누는 작업 도중 문제가 발생하여 해당 작업을 중지합니다.");
			e.printStackTrace();
			return;
		}
        
		WeatherDataVo weatherDataVo = new WeatherDataVo();
		
		weatherDataVo.setBreakerId(deviceIdtoString);
		weatherDataVo.setDir(getValue(segments.get(0)));
		weatherDataVo.setTmp(getValue(segments.get(1)));
		weatherDataVo.setHm(getValue(segments.get(2)));
		weatherDataVo.setWind(getValue(segments.get(3)));
		weatherDataVo.setGust(getValue(segments.get(4)));
		weatherDataVo.setRain(getValue(segments.get(5)));
		weatherDataVo.setUv(getValue(segments.get(6)));
		weatherDataVo.setLight(getValue(segments.get(7)));
		
		try {
			
			socketServerMapper.insertWeatherData(weatherDataVo);
			
		} catch (Exception e) {
			
			log.error("DB 에러");
			e.printStackTrace();
		}
	}
	
	/**
	 * 문자열 데이터 안에 숫자 부분만 추출.
	 * 
	 * @param byteData
	 * @return 성공시: 숫자 형태의 문자열<br>
	 * 		   실패시: null
	 */
	private String getValue(byte[] byteData) {
		
		String value = null;
		
		try {
			
			String stringData = new String(byteData);
			int index = stringData.indexOf(":");
			
			if (index != -1) { value = stringData.substring(index + 1); }
			
		} catch (Exception e) {
			
			log.error("데이터 추출중 에러 발생");
			e.printStackTrace();
		}
		
		return value;
	}
	
	/**
	 * 차량감지 신호 수신.
	 * 
	 * @param breakerId
	 * @param payload
	 */
	private void carDetectionEventReport(String breakerId, byte[] payload) {
		log.debug("carDetectionEventReport() 메서드 호출");
		
		taskExecutor.execute(() -> {
			
			log.debug("\"{}\" 차단기에서 차량 감지", breakerId);
			
			carDetectionBreakers.add(breakerId);
			log.debug("{} 차단기 차량감지 추가, {}", breakerId, carDetectionBreakers);
			
			try 							{ Thread.sleep(5000); } 
			catch (InterruptedException e) 	{ e.printStackTrace(); }
			
			carDetectionBreakers.remove(breakerId);
			log.debug("{} 차단기 차량감지 제거, {}", breakerId, carDetectionBreakers);
		});
	}
}
