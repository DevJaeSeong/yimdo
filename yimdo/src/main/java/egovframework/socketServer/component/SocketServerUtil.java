package egovframework.socketServer.component;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import egovframework.serverConfig.ServerConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 소켓통신 로직 수행에 쓰이는 유틸성 메소드를 가지고 있는 클래스.
 */
@Component("socketServerUtil")
@Slf4j
public class SocketServerUtil {
	
	private Map<String, Socket> socketMap = SocketServerContext.getSocketMap();
	private Set<String> runningBreakers = SocketServerContext.getRunningBreakers();
	private Map<String, Boolean> processingNormalOpen = SocketServerContext.getProcessingNormalOpen();
	
    /**
     * data가 유효한 데이터인지 검사하여 boolean으로 리턴.
     * @param data 받은 데이터
     * @return 모든 검사를 통과하면 true, 그렇지 않다면 false
     */
	public boolean validateData(byte[] data) {

		byte[] deviceId = { (byte) data[1], (byte) data[2] };
		String deviceIdtoString = deviceIdToString(deviceId);
		Socket socket = (Socket) socketMap.get(deviceIdtoString);
		boolean isDataValid = false;

		log.trace("통신이 이뤄진 소켓: {}", socket);

		if (data[0] != ServerConfig.PROTOCAL_ID)
			log.warn("식별되지 않은 Protocol ID.");
		else if (socket == null)
			log.warn("일치하는 deviceId 없음.");
		else if (socket.isClosed())
			log.warn("소켓이 닫혀있음.");
		else if ((data[5] & 0xff) != (data.length - ServerConfig.HEADER_LENGTH))
			log.warn("받은 데이터의 명시된 payload 크기와 수신한 payload 크기가 다름.");
		else
			isDataValid = true;

		log.trace("데이터 유효성 결과: {}", isDataValid);
		return isDataValid;
	}
	
	/**
	 * 보낼 데이터를 형식에 맞게 가공하여 byte[] 데이터로 반환
	 * @param breakerId 데이터를 보낼 차단기ID
	 * @param commandId 명령 식별자
	 * @param payloadLength 데이터 크기
	 * @param payload 데이터 내용
	 * @return 크기가 6 + payloadLength 인 byte[] 데이터
	 */
	public byte[] makeByteData(String breakerId, byte commandId, byte payloadLength, byte[] payload) {
		
		byte protocolId = ServerConfig.PROTOCAL_ID;
		byte[] deviceId = deviceIdToByteArray(breakerId);
		byte seq = (byte) 0x01;
		
		byte[] sendData = new byte[ServerConfig.HEADER_LENGTH + payload.length];
		sendData[0] = protocolId;
		sendData[1] = deviceId[0];
		sendData[2] = deviceId[1];
		sendData[3] = seq;
		sendData[4] = commandId;
		sendData[5] = payloadLength;
		
		for (int i = 0; i < payload.length; i++) {
			
			sendData[ServerConfig.HEADER_LENGTH + i] = payload[i];
		}
		
		return sendData;
	}
	
    /**
     * deviceId 부분 바이트 데이터를 4자리의 문자열로 변환.
     * @param deviceId 받은 데이터의 deviceID 부분
     * @return deviceId[0], deviceId[1]을 합친 4자리 문자열
     */
    public String deviceIdToString(byte[] deviceId) {
    	
    	String deviceId1 = Integer.toHexString(deviceId[0] & 0xff);
    	String deviceId2 = Integer.toHexString(deviceId[1] & 0xff);
    	
    	if (deviceId1.length() <= 1) deviceId1 = "0" + deviceId1;
    	if (deviceId2.length() <= 1) deviceId2 = "0" + deviceId2;
    	
    	String deviceIdtoString = deviceId1 + deviceId2;
    	
    	return deviceIdtoString;
    }
    
    /**
     * 4자리 문자열로된 deviceId 를 바이트 배열로 리턴.
     * @param 4자리 문자열로된 deviceId
     * @return 크기가 2인 byte[]
     */
    public byte[] deviceIdToByteArray(String deviceId) {
    	
    	String deviceId1 = deviceId.substring(0, 2);
    	String deviceId2 = deviceId.substring(2, 4);
    	
    	byte byteDeviceId1 = (byte) Integer.parseInt(deviceId1, 16);
    	byte byteDeviceId2 = (byte) Integer.parseInt(deviceId2, 16);
    	
    	byte[] deviceyteId = new byte[] {byteDeviceId1, byteDeviceId2};
    	
    	return deviceyteId;
    }
    
	
	/**
	 * 시간포멧 문자열을 나눠 두개의 dateTimeFormat을 맵에 담아 리턴
	 * @param dateTime 
	 * @return 0000-00-00 00:00:00 형태의 문자열과 식별번호를 담은 map(reciveDateTime, resDateTime) 을 리턴
	 */
	public Map<String, Object> dateTimeSplit(String dateTime) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		String reciveDateTime = "";
		String resDateTime = "";
		
		try {
			
			reciveDateTime = makeDateTimeFormat(dateTime.substring(0, 15));
			resDateTime = makeDateTimeFormat(dateTime.substring(15, 30));
			log.debug("reciveDateTime: {}", reciveDateTime);
			log.debug("resDateTime: {}", resDateTime);
			
		} catch (Exception e) {

			log.error("문자열 변환 실패.");
			e.printStackTrace();
		}
		
		map.put("reciveDateTime", reciveDateTime);
		map.put("resDateTime", resDateTime);
		
		return map;
	}
	
	/**
     * 00000000T000000 형태의 문자열을 0000-00-00 00:00:00 으로 변환하여 리턴.
     * @param dateTime 00000000T000000 형태의 문자열
     * @return 0000-00-00 00:00:00 형태의 문자열
     */
	public String makeDateTimeFormat(String dateTime) throws Exception {
		
		String year = dateTime.substring(0, 4);
		String month = dateTime.substring(4, 6);
		String day = dateTime.substring(6, 8);
		String hour = dateTime.substring(9, 11);
		String min = dateTime.substring(11, 13);
		String sec = dateTime.substring(13);
		
		/*
		log.debug("year: {}", year);
		log.debug("month: {}", month);
		log.debug("day: {}", day);
		log.debug("hour: {}", hour);
		log.debug("min: {}", min);
		log.debug("sec: {}", sec);
		*/
		
		String dateTimeFormat = year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;
		//log.debug("dateTimeFormat: {}", dateTimeFormat);
		
		return dateTimeFormat;
	}
	
	/**
	 * int => byte[]
	 * 
	 * @param value 바이트 배열로 변환할 정수
	 * @return 크기가 4인 byte[]
	 */
	public byte[] intToByteArray(int value) {
		
		byte[] byteArray = new byte[4];
		
	    byteArray[0] = (byte) (value >> 24);
	    byteArray[1] = (byte) (value >> 16);
	    byteArray[2] = (byte) (value >> 8);
	    byteArray[3] = (byte) value;
	    
	    return byteArray;
	}
	
	/**
	 * byte[] => int
	 * 
	 * @param byteArray int로 변환할 byte배열
	 * @return 변환된 int 타입 값
	 * @throws IllegalArgumentException byte배열의 크기가 4가 아닌경우
	 */
	public int byteArrayToInt(byte[] byteArray) throws IllegalArgumentException {
		
		if (byteArray.length != 4)
			throw new IllegalArgumentException("크기가 4인 바이트 배열만 가능합니다.");
		
		int value = 0;
		
		value = byteArray[0] << 24
			  | byteArray[1] << 16
			  | byteArray[2] << 8
			  | byteArray[3];
		
		return value;
	}
	
	/**
	 * byte[] 데이터 내용을 콘솔에 출력합니다.
	 * @param data 받은 byte[] 데이터
	 */
	public void printByteData(byte[] data) {
		
        StringBuilder sb = new StringBuilder("[");
        
        for (byte b : data) {
        	
            //sb.append(String.format("0x%02X", b & 0xFF)).append(", ");
            sb.append(String.format("%02X", b & 0xFF)).append(", ");
        }
        
        sb.setLength(sb.length() - 2);
        sb.append("]");

        log.debug("{}", sb.toString());
	}
	
	/**
	 * byte[] 데이터 내용을 콘솔에 출력합니다.
	 * @param data 받은 byte[] 데이터
	 */
	public void printByteData(byte[] data, String word) {
		
        StringBuilder sb = new StringBuilder("[");
        
        for (byte b : data) {
        	
            //sb.append(String.format("0x%02X", b & 0xFF)).append(", ");
            sb.append(String.format("%02X", b & 0xFF)).append(", ");
        }
        
        sb.setLength(sb.length() - 2);
        sb.append("]");

        log.debug("{}: {}", word, sb.toString());
	}
	
	public boolean validateBreakerStatus(String breakerId) {
		
		if (socketMap.get(breakerId) == null) {
			
			log.error("\"{}\" 차단기가 연결되어있지 않습니다.", breakerId);
			return false;
		}
		
		if (runningBreakers.contains(breakerId)) {
			
			log.warn("해당 차단기가 이전 요청을 처리중입니다.");
			return false;
		}
		
		if (processingNormalOpen.containsKey(breakerId) && processingNormalOpen.get(breakerId)) {
			
			log.warn("해당 차단기가 정상개방 요청을 처리중입니다.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * 웹 소켓 핸드셰이크 요청인지 확인.
	 * 
	 * @param request 받은 데이터를 문자열로 변환한것
	 * @return
	 */
    public boolean isWebSocketHandshake(String request) {
    	
        // 웹 소켓 핸드셰이크는 Upgrade 헤더와 Sec-WebSocket-Key 헤더를 포함
        return request.contains("Upgrade: websocket") && request.contains("Sec-WebSocket-Key:");
    }
    
    /**
     * 바이트 배열을 특정 데이터를 기준으로 나눠서 반환.
     * 
     * @param source 나누려하는 byte[]
     * @param delimiter 배열을 나눌 기준 byte 데이터
     * @return delimiter를 기준으로 나눠진 byte[]를 담은 List
     * @throws Exception
     */
    public List<byte[]> splitByteArray(byte[] source, byte delimiter) throws Exception {
    	
        List<byte[]> result = new ArrayList<>();
        int start = 0;

        for (int i = 0; i < source.length; i++) {
        	
            if (source[i] == delimiter) {
            	
                byte[] segment = Arrays.copyOfRange(source, start, i);
                result.add(segment);
                start = i + 1;
            }
        }

        byte[] lastSegment = Arrays.copyOfRange(source, start, source.length);
        result.add(lastSegment);

        return result;
    }
}
