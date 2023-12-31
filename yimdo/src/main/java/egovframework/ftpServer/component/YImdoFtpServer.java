package egovframework.ftpServer.component;

import java.io.IOException;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.impl.DefaultFtpServer;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.SaltedPasswordEncryptor;
import org.apache.ftpserver.usermanager.impl.PropertiesUserManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import egovframework.serverConfig.ServerConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * FTP 서버 구축 클래스 입니다.
 */
@Component
@Slf4j
public class YImdoFtpServer {
	
	public void openServer() {
		
		FtpServer ftpServer = null;
		
		try {
			
			ftpServer = createFtpServer();
			ftpServer.start();
			log.debug("\"{}\" 포트로 FTP 서버 시작.", ServerConfig.FTP_PORT);
			
			Thread.sleep(Long.MAX_VALUE);
			
		} catch (Exception e) {

			log.error("FTP 서버 문제 발생.");
			e.printStackTrace();
			
		} finally {
			
            if (ftpServer != null && !ftpServer.isStopped()) {
            	
                ftpServer.stop();
                log.debug("FTP 서버 종료.");
            }
		}
	}

	/**
	 * ftp 서버 생성.
	 * 
	 * @return new {@link DefaultFtpServer}
	 * @throws IOException
	 * @throws FtpException
	 */
	private FtpServer createFtpServer() throws IOException, FtpException {
		
        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(ServerConfig.FTP_PORT);
        
        FtpServerFactory ftpServerFactory = new FtpServerFactory();
        ftpServerFactory.addListener("default", listenerFactory.createListener());
        ftpServerFactory.setUserManager(createUserManager());
        
		return ftpServerFactory.createServer();
	}

	/**
	 * ftp 서버에 접근가능한 유저 목록.
	 * 
	 * @return new {@link PropertiesUserManager}
	 * @throws IOException
	 * @throws FtpException
	 */
	private UserManager createUserManager() throws IOException, FtpException {
		
        PropertiesUserManagerFactory propertiesUserManagerFactory = new PropertiesUserManagerFactory();
        propertiesUserManagerFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
        propertiesUserManagerFactory.setFile(new ClassPathResource(ServerConfig.GLOBALS_PROPERTIES_RESOURCE_PATH).getFile());
		
        return propertiesUserManagerFactory.createUserManager();
	}
}
