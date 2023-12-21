package egovframework.ftpServer.controller;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import egovframework.ftpServer.component.YImdoFtpServer;

@Controller
public class FtpServerController {

	@Resource(name = "yImdoFtpServer")
	private YImdoFtpServer yImdoFtpServer;
	
	@Scheduled(initialDelay = 5000, fixedDelay = Long.MAX_VALUE)
	private void serverStart() {
		
		yImdoFtpServer.openServer();
	}
	
	/*
	@GetMapping("/general/ftp")
	@ResponseBody
	public Object ftp(@RequestParam("pw") String pw) {
	
		ClearTextPasswordEncryptor clearTextPasswordEncryptor = new ClearTextPasswordEncryptor();
		Md5PasswordEncryptor md5PasswordEncryptor = new Md5PasswordEncryptor();
		SaltedPasswordEncryptor saltedPasswordEncryptor = new SaltedPasswordEncryptor();
		
		Map<String, String> result = new HashMap<String, String>();
		
		result.put("clearText", clearTextPasswordEncryptor.encrypt(pw));
		result.put("md5", md5PasswordEncryptor.encrypt(pw));
		result.put("salted", saltedPasswordEncryptor.encrypt(pw));
		
		return result;
	}
	*/
}
