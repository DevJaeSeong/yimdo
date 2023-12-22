package egovframework.socketServer.controller;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import egovframework.serverConfig.ServerConfig;
import egovframework.socketServer.component.SocketServer;
import egovframework.socketServer.component.SocketServerContext;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SocketServerController {
	
	@Resource(name = "socketServer")
	private SocketServer socketServer;
	
	@Scheduled(initialDelay = 5000, fixedDelay = Long.MAX_VALUE)
	private void serverStart() {
		
		while (ServerConfig.isServerRunning) { 
			
			socketServer.openSocket(); 
		}
	}
	
	@GetMapping("/general/getSocket")
	public void getSocket() {
		
		log.debug("{}", SocketServerContext.getSocketMap());
	}
	
}
