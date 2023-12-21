package egovframework.api.apies;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import egovframework.api.vo.RiseSetInfoVo;
import egovframework.common.GlobalUtil;
import egovframework.serverConfig.ServerConfig;
import lombok.extern.slf4j.Slf4j;

@Component("riseSetInfoAPI")
@Slf4j
public class RiseSetInfoAPI extends AbstractApiForm {
	
	private String url = "http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getAreaRiseSetInfo";
	private String serviceKey = ServerConfig.API_KEY;
	private String pageNo = "1";
	private String numOfRows = "999";
	private String location = "서울";
	private String locdate = GlobalUtil.getDateFormat();

	@Override
	public String getUrl() throws UnsupportedEncodingException {
		log.trace("출몰시각 정보제공 서비스 API 호출");
		
		StringBuilder urlBuilder = new StringBuilder(url);
		
		urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
		urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8")); 
		urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8")); 
		urlBuilder.append("&" + URLEncoder.encode("location", "UTF-8") + "=" + URLEncoder.encode(location, "UTF-8"));
		urlBuilder.append("&" + URLEncoder.encode("locdate", "UTF-8") + "=" + URLEncoder.encode(locdate, "UTF-8"));
		
		return urlBuilder.toString();
	}

	@Override
	public List<?> parseData(String data) throws Exception {
		log.trace("데이터 파싱 시작");
		
		List<RiseSetInfoVo> riseSetInfoVos = new ArrayList<RiseSetInfoVo>();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new InputSource(new StringReader(data)));
		
		if (document == null) { 
			
			log.debug("데이터 없음");
			return null; 
		}
		
		NodeList itemsList = document.getElementsByTagName("items");
		
		for (int i = 0; i < itemsList.getLength(); i ++) {
			
			Element item = (Element) itemsList.item(i);
			
			RiseSetInfoVo riseSetInfoVo = new RiseSetInfoVo();
			
			riseSetInfoVo.setR_aste(getDataByElement(item, "aste"));
			riseSetInfoVo.setR_astm(getDataByElement(item, "astm"));
			riseSetInfoVo.setR_civile(getDataByElement(item, "civile"));
			riseSetInfoVo.setR_civilm(getDataByElement(item, "civilm"));
			riseSetInfoVo.setR_latitude(getDataByElement(item, "latitude"));
			riseSetInfoVo.setR_latitudeNum(getDataByElement(item, "latitudeNum"));
			riseSetInfoVo.setR_location(getDataByElement(item, "location"));
			riseSetInfoVo.setR_locdate(getDataByElement(item, "locdate"));
			riseSetInfoVo.setR_longitude(getDataByElement(item, "longitude"));
			riseSetInfoVo.setR_longitudeNum(getDataByElement(item, "longitudeNum"));
			riseSetInfoVo.setR_moonrise(getDataByElement(item, "moonrise"));
			riseSetInfoVo.setR_moonset(getDataByElement(item, "moonset"));
			riseSetInfoVo.setR_moontransit(getDataByElement(item, "moontransit"));
			riseSetInfoVo.setR_naute(getDataByElement(item, "naute"));
			riseSetInfoVo.setR_nautm(getDataByElement(item, "nautm"));
			riseSetInfoVo.setR_sunrise(getDataByElement(item, "sunrise"));
			riseSetInfoVo.setR_sunset(getDataByElement(item, "sunset"));
			riseSetInfoVo.setR_suntransit(getDataByElement(item, "suntransit"));
			
			riseSetInfoVos.add(riseSetInfoVo);
		}
		
		log.trace("데이터 파싱 끝");
		return riseSetInfoVos;
	}
}
