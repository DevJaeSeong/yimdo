package egovframework.socketServer.component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	private ServerSocket serverSocket;
	private final Map<String, Socket> socketMap = SocketServerContext.getSocketMap();
	private final Set<String> runningBreakers = SocketServerContext.getRunningBreakers();
	
	private TaskExecutor taskExecutor;
	private SocketServerUtil socketServerUtil;
	private SocketServerReceiver socketServerReceiver;
	
	@Autowired
	public SocketServer(TaskExecutor taskExecutor,
						SocketServerUtil socketServerUtil,
						SocketServerReceiver socketServerReceiver) {
		
		this.taskExecutor = taskExecutor;
		this.socketServerUtil = socketServerUtil;
		this.socketServerReceiver = socketServerReceiver;
	}
	
	/**
	 * 소켓 서버 시작.
	 */
    public void openSocket() {
    	log.debug("openSocket() 시작");
    	
    	try (ServerSocket serverSocket = new ServerSocket(ServerConfig.SOCKET_PORT)) {
    		
    		this.serverSocket = serverSocket;
    		log.debug("\"{}\"포트 서버소켓 개방", ServerConfig.SOCKET_PORT);
    		
    		/*
    		 * 1. accept() 메서드는 ServerConfig.socketPort 포트로 소켓연결이 될때까지 block 상태를 유지하다가 연결요청이 들어오면 Socket 반환.
    		 * 2. 반환된 Socket을 인자값으로 SocketServerRunnable 생성.
    		 * 3. TaskExecutor는 SocketServerRunnable의 run() 메서드를 비동기로 실행. (여러 작업을 동시에 처리)
    		 */
    		while (ServerConfig.isServerRunning) { 
    			
    			try {
					
    				Socket socket = serverSocket.accept();	// 연결 요청이 들어올때까지 block, 연결에 성공하면 Socket 객체를 반환하고 로직 진행.
    				log.debug("연결된 socket: {}", socket);
    				
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
		}
		
		@Override
		public void run() {
			log.debug("run() 시작");
			
			String breakerId = null;
			
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
					
					byteArrayOutputStream.reset();
					byteArrayOutputStream.write(buffer, 0, bufferLen);
					
					byte[] data = byteArrayOutputStream.toByteArray();
					breakerId = socketServerUtil.hexDataToString((byte) data[1], (byte) data[2]);
					
					log.debug("데이터 수신: {}", socketServerUtil.printByteDataToString(data));
					
                	if (!socketMap.containsKey(breakerId)) {
                		
                		socketMap.put(breakerId, socket);
                		log.debug("\"{}\" 차단기에 연결된 소켓 socketMap에 추가 => socketMap: {}", breakerId, socketMap);
                	}
                	
                	if (socketServerUtil.validateData(data))
                		socketServerReceiver.processReceivedData(data, breakerId);
				}
			
			} catch (Exception e) {

				log.error("소켓 처리 중 에러발생");
				e.printStackTrace();
				
			} finally {
				
				try {
					
					socket.close();
					log.debug("\"{}\" 차단기에 연결된 소켓 연결 종료.", breakerId);
					
				} catch (Exception e) {
					
					log.error("소켓 닫기 중 에러발생");
					e.printStackTrace();
				}
				
				if (breakerId != null) {
					
					if (socketMap.containsKey(breakerId)) {
						
						socketMap.remove(breakerId);
						log.debug("\"{}\" 차단기에 연결된 소켓 socketMap에 제거 => socketMap: {}", breakerId, socketMap);
					}
					
					if (runningBreakers.contains(breakerId)) {
						
						runningBreakers.remove(breakerId);
						log.debug("\"{}\" 차단기 runningBreakers에서 제거 => runningBreakers: {}", breakerId, runningBreakers);
					}
				}
			}
			
			log.debug("run() 끝");
		}
	}
	
	/**
	 * 서버가 종료될때 서버소켓 연결 종료.
	 */
	@PreDestroy
	private void closeSocket() {
		
		ServerConfig.isServerRunning = false;
		
		try {
			
			serverSocket.close();
			log.debug("서버소켓 연결 종료.");
			
		} catch (Exception e) {
			
			log.error("서버소켓 종료 실패.");
			e.printStackTrace();
		}
	}
}
