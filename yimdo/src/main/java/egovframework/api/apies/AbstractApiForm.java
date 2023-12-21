package egovframework.api.apies;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.w3c.dom.Element;

import egovframework.common.GlobalUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 공공데이터API 호출하는 클래스 구현시 반드시 상속받아야 하는 추상클래스.
 */
@Slf4j
public abstract class AbstractApiForm {
	
	/**
	 * API에 데이터 요청을 위한 url을 반환합니다
	 * @return 문자열로된 url
	 */
	public abstract String getUrl() throws UnsupportedEncodingException;
	
	/**
	 * JSON/XML 형태의 문자열 데이터를 파싱하여 Vo객체로 만든 후 Vo객체들을 담은 List를 반환.
	 * @param data JSON/XML 형태의 데이터 문자열
	 * @return 성공시: Vo 객체를 담은 List<br>
	 * 		   실패시: null
	 */
	public abstract List<?> parseData(String data) throws Exception;
	
	/**
	 * 받은 url으로 API를 호출하여 데이터를 획득.
	 * @param url API에 데이터 요청을 위한 url
	 * @return 성공시: Map("vos": 해당 API의 데이터를 담은 Vo 리스트, "req_date": API호출 시각 문자열)<br>
	 * 	       실패시: null
	 */
	public Map<String, Object> getData(String url) throws Exception {

		Map<String, Object> datas = callAPI(url);
		
		List<?> vos = parseData((String) datas.get("data"));
		String req_date = (String) datas.get("req_date");
		
		if (vos == null) { return null; }
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("vos", vos);
		map.put("req_date", req_date);
		
		return map;
	}
	
	/**
	 * 
	 * @param url
	 * @return Map("data": 받은 JSON/XML 문자열 데이터, "req_date": API호출 시각 문자열)
	 * @throws Exception
	 */
	public Map<String, Object> callAPI(String url) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("req_date", GlobalUtil.getNowDateTime());
		
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("GET");
		
		try (BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			
			StringBuilder sb = new StringBuilder();
			String line;
			
			while ((line = rd.readLine()) != null) {
				
				sb.append(line);
			}
			
			map.put("data", sb.toString());
		}
		
		connection.disconnect();
		
		return map;
	}
	
	/**
	 * json 데이터를 파싱해서 얻은 {@link JSONObject}객체에서 key으로 추출한 데이터를 문자열로 반환합니다.
	 * @param item 데이터가 담겨있는 객체
	 * @param key 획득할 데이터의 key
	 * @return 문자열로 된 데이터값(데이터가 null인 경우 "null" 문자열로 반환)
	 */
	protected String getDataByJSONObject(JSONObject jsonObject, String key) {
		
		String result;
		
		try {
			
			result = String.valueOf(jsonObject.get(key));
			
		} catch (Exception e) {
			
			result = "null";
			log.error("에러발생");
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * xml 데이터를 파싱해서 얻은 {@link Element}객체에서 tagName으로 추출한 데이터를 문자열로 반환합니다.
	 * @param item 데이터가 담겨있는 객체
	 * @param tagName 획득할 데이터의 tagName
	 * @return 문자열로 된 데이터값(데이터가 null인 경우 "null" 문자열로 반환)
	 */
	protected String getDataByElement(Element item, String tagName) {
		
		String result;
		
		try {
			
			result = String.valueOf(item.getElementsByTagName(tagName).item(0).getTextContent()).trim();
			
		} catch (Exception e) {

			result = "null";
			log.error("에러발생");
			e.printStackTrace();
		}
		
		return result;
	}
}
