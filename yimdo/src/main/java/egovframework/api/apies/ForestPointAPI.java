package egovframework.api.apies;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import egovframework.api.vo.ForestPointVo;
import egovframework.serverConfig.ServerConfig;
import lombok.extern.slf4j.Slf4j;

@Component("forestPointAPI")
@Slf4j
public class ForestPointAPI extends AbstractApiForm {
	
	private String url = "http://apis.data.go.kr/1400377/forestPoint/forestPointListSigunguSearch";
	private String serviceKey = ServerConfig.API_KEY;
	private String pageNo = "1";
	private String numOfRows = "999";
	private String _type = "json";
	private String upplocalcd = "30";

	@Override
	public String getUrl() throws UnsupportedEncodingException {
		log.trace("산불위험예보정보API 호출");
		
		StringBuilder urlBuilder = new StringBuilder(url);
		
		urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
		urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8")); 
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8")); 
		urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode(_type, "UTF-8")); 
		urlBuilder.append("&" + URLEncoder.encode("upplocalcd", "UTF-8") + "=" + URLEncoder.encode(upplocalcd, "UTF-8"));
		
		return urlBuilder.toString();
	}

	@Override
	public List<?> parseData(String data) throws Exception {
		log.trace("데이터 파싱 시작");
		
		List<ForestPointVo> forestPointVos = new ArrayList<ForestPointVo>();

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
			
			ForestPointVo forestPointVo = new ForestPointVo();
			
			forestPointVo.setF_analdate(getDataByJSONObject(object, "analdate"));
			forestPointVo.setF_area(getDataByJSONObject(object, "area"));
			forestPointVo.setF_d1(getDataByJSONObject(object, "d1"));
			forestPointVo.setF_d2(getDataByJSONObject(object, "d2"));
			forestPointVo.setF_d3(getDataByJSONObject(object, "d3"));
			forestPointVo.setF_d4(getDataByJSONObject(object, "d4"));
			forestPointVo.setF_doname(getDataByJSONObject(object, "doname"));
			forestPointVo.setF_maxi(getDataByJSONObject(object, "maxi"));
			forestPointVo.setF_meanavg(getDataByJSONObject(object, "meanavg"));
			forestPointVo.setF_mini(getDataByJSONObject(object, "mini"));
			forestPointVo.setF_regioncode(getDataByJSONObject(object, "regioncode"));
			forestPointVo.setF_sigucode(getDataByJSONObject(object, "sigucode"));
			forestPointVo.setF_sigun(getDataByJSONObject(object, "sigun"));
			forestPointVo.setF_std(getDataByJSONObject(object, "std"));
			forestPointVo.setF_upplocalcd(getDataByJSONObject(object, "upplocalcd"));
			
			forestPointVos.add(forestPointVo);
		}

		log.trace("데이터 파싱 끝");
		return forestPointVos;
	}

}
