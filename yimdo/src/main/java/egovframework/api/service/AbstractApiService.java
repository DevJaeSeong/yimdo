package egovframework.api.service;

import java.net.SocketException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.api.apies.AbstractApiForm;

public abstract class AbstractApiService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * api를 주입하면 주입된 api를 호출하여 받은 데이터를 db에 저장
	 */
	public void doDataProccessing(AbstractApiForm api) {
		log.trace("doDataProccessing() 시작");
		
		Map<String, Object> map = null;
		
		try {
			
			String url = api.getUrl();
			
			map = api.getData(url);
			
		} catch (SocketException e) {
			
			log.debug("데이터 파싱중 문제발생: {}", e.getMessage());
			
		} catch (Exception e) {
			
			log.error("데이터 파싱중 문제발생.");
			e.printStackTrace();
			return;
		}
		
		if (map != null) { insertData(map, api); }
		
		log.trace("doDataProccessing() 끝");
	}
	
	public abstract void insertData(Map<String, Object> data, AbstractApiForm api);
}
