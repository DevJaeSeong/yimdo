package egovframework.socketServer.controller;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import egovframework.serverConfig.ServerConfig;
import egovframework.socketServer.component.SocketServer;

@Controller
public class SocketServerController {
	
	@Resource(name = "socketServer")
	private SocketServer socketServer;
	
	@Scheduled(initialDelay = 5000, fixedDelay = Long.MAX_VALUE)
	private void serverStart() {
		
		while (ServerConfig.isServerRunning) { 
			
			socketServer.openSocket(); 
		}
	}
}
