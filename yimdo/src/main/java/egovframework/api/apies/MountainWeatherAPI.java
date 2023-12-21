package egovframework.api.apies;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import egovframework.api.vo.MountainWeatherVo;
import egovframework.serverConfig.ServerConfig;
import lombok.extern.slf4j.Slf4j;

@Component("mountainWeatherAPI")
@Slf4j
public class MountainWeatherAPI extends AbstractApiForm {
	
	private String serviceKey = ServerConfig.API_KEY;
	private String pageNo = "1";
	private String numOfRows = "999";
	private String type = "json";
	//private String localArea = "07";

	@Override
	public String getUrl() throws UnsupportedEncodingException {
		log.trace("산악기상정보API 호출");
		
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1400377/mtweather/mountListSearch");
		
		urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
		urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8")); 
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8")); 
		urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")); 
		//urlBuilder.append("&" + URLEncoder.encode("localArea", "UTF-8") + "=" + URLEncoder.encode(localArea, "UTF-8"));
		
		return urlBuilder.toString();
	}

	@Override
	public List<MountainWeatherVo> parseData(String data) throws Exception {
		log.trace("데이터 파싱 시작");
		
		List<MountainWeatherVo> mountainWeatherVos = new ArrayList<MountainWeatherVo>();

		JSONObject jsonObject = new JSONObject(data);
		JSONObject response = (JSONObject) jsonObject.get("response");
		JSONObject body = (JSONObject) response.get("body");
		
		if (body == null) { 
			
			log.debug("데이터 없음");
			return null; 
		}
		
		JSONObject items = (JSONObject) body.get("items");
		JSONArray item = (JSONArray) items.get("item");
		
		for (Object objectItem : item) {
			
			JSONObject object = (JSONObject) objectItem;
			
			MountainWeatherVo mountainWeatherVo = new MountainWeatherVo();
			
			mountainWeatherVo.setM_localarea(getDataByJSONObject(object, "localarea"));
			mountainWeatherVo.setM_obsid(getDataByJSONObject(object, "obsid"));
			mountainWeatherVo.setM_obsname(getDataByJSONObject(object, "obsname"));
			mountainWeatherVo.setM_tm(getDataByJSONObject(object, "tm"));
			mountainWeatherVo.setM_cprn(getDataByJSONObject(object, "cprn"));
			mountainWeatherVo.setM_rn(getDataByJSONObject(object, "rn"));
			mountainWeatherVo.setM_pa(getDataByJSONObject(object, "pa"));
			mountainWeatherVo.setM_ts(getDataByJSONObject(object, "ts"));
			mountainWeatherVo.setM_hm10m(getDataByJSONObject(object, "hm10m"));
			mountainWeatherVo.setM_hm2m(getDataByJSONObject(object, "hm2m"));
			mountainWeatherVo.setM_tm10m(getDataByJSONObject(object, "tm10m"));
			mountainWeatherVo.setM_tm2m(getDataByJSONObject(object, "tm2m"));
			mountainWeatherVo.setM_wd10m(getDataByJSONObject(object, "wd10m"));
			mountainWeatherVo.setM_wd10mstr(getDataByJSONObject(object, "wd10mstr"));
			mountainWeatherVo.setM_wd2m(getDataByJSONObject(object, "wd2m"));
			mountainWeatherVo.setM_wd2mstr(getDataByJSONObject(object, "wd2mstr"));
			mountainWeatherVo.setM_ws10m(getDataByJSONObject(object, "ws10m"));
			mountainWeatherVo.setM_ws2m(getDataByJSONObject(object, "ws2m"));
			
			mountainWeatherVos.add(mountainWeatherVo);
		}

		log.trace("데이터 파싱 끝");
		return mountainWeatherVos;
	}
}
