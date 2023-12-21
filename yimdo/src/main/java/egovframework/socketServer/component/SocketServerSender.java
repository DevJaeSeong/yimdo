package egovframework.socketServer.component;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 소켓통신을 통해 데이터 전송을 처리하는 클래스.
 */
@Component("socketServerSender")
@Slf4j
public class SocketServerSender {

	@Resource(name = "socketServerUtil")
	private SocketServerUtil socketServerUtil;
	
	private Map<String, Socket> socketMap = SocketServerContext.getSocketMap();
	private Set<String> runningBreakers = SocketServerContext.getRunningBreakers();
	
	/**
	 * 차단기 상태 변경 요청이 들어오면 요청을 차단기에 전달.
	 * @param breakerId 요청을 보낼 차단기ID
	 * @param breakerPolicyCode 요청을 보낼 정책코드
	 * @param breakerHistoryId 차단기관리이력 해당 데이터 PK값
	 * @return 성공하면 true 실패하면 false 리턴
	 */
	public boolean sendRequestStatus(String breakerId, String breakerPolicyCode, int breakerHistoryId) {
		log.debug("sendRequestStatus() 시작");
		log.debug("<== breakerId: {}, breakerPolicyCode: {}, breakerHistoryId: {}", breakerId, breakerPolicyCode, breakerHistoryId);
		
		if (!socketServerUtil.validateBreakerStatus(breakerId)) {
			
			log.debug("sendRequestStatus() 끝");
			return false;
		}
		
		Socket socket = (Socket) socketMap.get(breakerId);
		
		byte[] breakerHistoryIdtoByteArray = socketServerUtil.intToByteArray(breakerHistoryId);
		byte commandId;
		byte paylodadLength = (byte) 0x05;
		byte[] payload = new byte[paylodadLength & 0xff];
		
		switch (breakerPolicyCode) {
		
			case "1001":
				commandId = (byte) 0x24;
				payload[0] = (byte) 0x01;
				break;
			
			case "1002":
				commandId = (byte) 0x24;
				payload[0] = (byte) 0x02;
				break;
				
			case "2001":
				commandId = (byte) 0x25;
				payload[0] = (byte) 0x01;
				break;
				
			case "2002":
				commandId = (byte) 0x25;
				payload[0] = (byte) 0x02;
				break;
	
			default:
				log.warn("알 수 없는 commandId: {}", breakerPolicyCode);
				log.debug("sendRequestStatus() 끝");
				return false;
		}
		
		boolean result = false;
		
		try {
			
			for (int i = 0; i < breakerHistoryIdtoByteArray.length; i++) {
				
				payload[i + 1] = breakerHistoryIdtoByteArray[i];
			}
			
			byte[] sendData = socketServerUtil.makeByteData(breakerId, commandId, paylodadLength, payload);
			
			OutputStream out = socket.getOutputStream();
			out.write(sendData);
			out.flush();
			
			socketServerUtil.printByteData(sendData, "보내는 데이터");
			
			runningBreakers.add(breakerId);
			
			result = true;
			
		} catch (Exception e) {
			
			log.error("sendRequestStatus() 처리중 에러발생");
			e.printStackTrace();
		}
		
		log.debug("sendRequestStatus() 끝");
		return result;
	}
}
