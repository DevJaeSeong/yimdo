package egovframework.socketServer.component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import egovframework.serverConfig.ServerConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 차단기와 소켓 통신을 위한 클래스.
 */
@Component("socketServer")
@Slf4j
public class SocketServer {
	
	/** 멀티스레드 환경을 이용해 다수의 작업을 비동기적으로 처리하는 클래스. */
	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;
	
	@Resource(name = "socketServerUtil")
	private SocketServerUtil socketServerUtil;
	
	@Resource(name = "socketServerReceiver")
	private SocketServerReceiver socketServerReceiver;
	
	/** 지정된 포트로 개방된 소켓서버를 저장한 맵 */
	private Map<Integer, ServerSocket> serverSocketMap = new ConcurrentHashMap<Integer, ServerSocket>();
	private Map<String, Socket> socketMap = SocketServerContext.getSocketMap();
	
	/**
	 * 소켓 서버 시작.
	 */
    public void openSocket() {
    	log.debug("openSocket() 시작");
    	
    	try (ServerSocket serverSocket = new ServerSocket(ServerConfig.SOCKET_PORT)) {
    		log.debug("{}포트 소켓 개방", ServerConfig.SOCKET_PORT);
    		
    		serverSocketMap.put(ServerConfig.SOCKET_PORT, serverSocket);
    		
    		/*
    		 * 1. accept() 메서드는 ServerConfig.socketPort 포트로 소켓연결이 될때까지 block 상태를 유지하다가 연결요청이 들어오면 Socket 반환.
    		 * 2. 반환된 Socket을 인자값으로 SocketServerRunnable 생성.
    		 * 3. TaskExecutor는 SocketServerRunnable의 run() 메서드를 비동기로 실행. (여러 작업을 동시에 처리)
    		 */
    		while (ServerConfig.isServerRunning) { 
    			
    			try {
					
    				Socket socket = serverSocket.accept();	// 연결 요청이 들어올때까지 block, 연결에 성공하면 Socket 객체를 반환하고 로직 진행.
    				taskExecutor.execute(new SocketServerRunnable(socket)); 
    				
				} catch (Exception e) {
					
					if (Thread.interrupted()) {
						
						log.debug("해당 스레드가 block 상태에서 중지 요청이 들어왔습니다.");
						
					} else {
						
						log.error("에러 발생");
						e.printStackTrace();
					}
				}
    		}
    		
		} catch (Exception e) {
			
			log.error("{}포트 소켓 개방중 오류발생", ServerConfig.SOCKET_PORT);
			e.printStackTrace();
		}
    	
    	log.debug("openSocket() 끝");
    }
	
	/**
	 * {@link TaskExecutor}의 작업내용을 정의한 {@link Runnable}의 구현체.
	 */
	private class SocketServerRunnable implements Runnable {

		private Socket socket;
		
		public SocketServerRunnable(Socket socket) {
			
			this.socket = socket;
			log.debug("연결된 socket: {}", socket);
		}
		
		@Override
		public void run() {
			log.debug("run() 시작");
			
			String deviceIdtoString = null;
			
			try {
				
				InputStream inputStream = socket.getInputStream();
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				
				byte[] buffer = new byte[1024];	// 수신 데이터량에 맞게 크기 조정
				int bufferLen = 0;
				
				/*
				 * read() 메소드는 데이터가 들어올때까지 blocking 되어이있다가 데이터가 들어오면 읽기 로직 수행.
				 * 연결이 종료되어 더이상 읽을 데이터가 없다면 -1을 반환.
				 * 데이터를 보내지 않아도 연결되어있다면 -1을 반환하는게 아니라 blocking 되어 작업 대기.
				 */
				while ((bufferLen = inputStream.read(buffer)) != -1) {
					log.trace("============================================================");
					
					byteArrayOutputStream.reset();
					byteArrayOutputStream.write(buffer, 0, bufferLen);
					
					byte[] data = byteArrayOutputStream.toByteArray();
					
					socketServerUtil.printByteData(data, "수신 데이터");
					
                	byte[] deviceId = {(byte) data[1], (byte) data[2]};
                	deviceIdtoString = socketServerUtil.deviceIdToString(deviceId);
                	
                	if (!socketMap.containsKey(deviceIdtoString))
                		socketMap.put(deviceIdtoString, socket);
                	
                	if (socketServerUtil.validateData(data))
                		socketServerReceiver.processIncomingData(data);
                	
                	log.trace("============================================================");
				}
			
			} catch (Exception e) {

				log.error("소켓 처리 중 에러발생");
				e.printStackTrace();
				
			} finally {
				
				try {
					
					log.debug("{} 소켓 닫힘", socket);
					socket.close();
					socketMap.remove(deviceIdtoString);
                    
				} catch (Exception e) {
					
					log.error("소켓 닫기 중 에러발생");
					e.printStackTrace();
				}
			}
			
			log.debug("run() 끝");
		}
	}
	
	/**
	 * 서버가 종료될때 {@linkplain socketPort} 포트로 연결된 소켓 닫음.
	 */
	@PreDestroy
	private void closeSocket() {
		
		ServerConfig.isServerRunning = false;
		log.debug("서버 종료 이벤트 발생");
		
		serverSocketMap.forEach((port, serverSocket) -> {
			
			try {
				
				serverSocket.close();
				log.debug("{} 포트 소켓 닫음", port);
				
			} catch (Exception e) {
				
				log.error("소켓닫기 실패");
				e.printStackTrace();
			}
		});
	}
}
