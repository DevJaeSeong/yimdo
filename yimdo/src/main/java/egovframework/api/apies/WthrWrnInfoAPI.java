package egovframework.api.apies;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import egovframework.api.vo.WthrWrnInfoVo;
import egovframework.serverConfig.ServerConfig;
import lombok.extern.slf4j.Slf4j;

@Component("wthrWrnInfoAPI")
@Slf4j
public class WthrWrnInfoAPI extends AbstractApiForm {
	
	private String url = "http://apis.data.go.kr/1360000/WthrWrnInfoService/getPwnCd";
	private String serviceKey = ServerConfig.API_KEY;
	private String pageNo = "1";
	private String numOfRows = "999";
	private String dataType = "json";

	@Override
	public String getUrl() throws UnsupportedEncodingException {
		log.trace("출몰시각 정보제공 서비스 API 호출");
		
		StringBuilder urlBuilder = new StringBuilder(url);
		
		urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
		urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8")); 
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8")); 
		urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8")); 
		
		return urlBuilder.toString();
	}

	@Override
	public List<?> parseData(String data) throws Exception {
		log.trace("데이터 파싱 시작");
		
		List<WthrWrnInfoVo> wthrWrnInfoVos = new ArrayList<WthrWrnInfoVo>();

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
			
			WthrWrnInfoVo wthrWrnInfoVo = new WthrWrnInfoVo();
			wthrWrnInfoVo.setW_allEndTime(getDataByJSONObject(object, "allEndTime"));
			wthrWrnInfoVo.setW_areaCode(getDataByJSONObject(object, "areaCode"));
			wthrWrnInfoVo.setW_areaName(getDataByJSONObject(object, "areaName"));
			wthrWrnInfoVo.setW_cancel(getDataByJSONObject(object, "cancel"));
			wthrWrnInfoVo.setW_command(getDataByJSONObject(object, "command"));
			wthrWrnInfoVo.setW_endTime(getDataByJSONObject(object, "endTime"));
			wthrWrnInfoVo.setW_stnId(getDataByJSONObject(object, "stnId"));
			wthrWrnInfoVo.setW_tmFc(getDataByJSONObject(object, "tmFc"));
			wthrWrnInfoVo.setW_tmSeq(getDataByJSONObject(object, "tmSeq"));
			wthrWrnInfoVo.setW_warnVar(getDataByJSONObject(object, "warnVar"));
			wthrWrnInfoVo.setW_warnStress(getDataByJSONObject(object, "warnStress"));
			wthrWrnInfoVo.setW_startTime(getDataByJSONObject(object, "startTime"));
			
			wthrWrnInfoVos.add(wthrWrnInfoVo);
		}

		log.trace("데이터 파싱 끝");
		return wthrWrnInfoVos;
	}
}
