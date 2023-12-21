package egovframework.yimdoSystem.apiDataFetcher;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import egovframework.api.vo.MountainWeatherVo;
import egovframework.api.vo.RiseSetInfoVo;
import lombok.extern.slf4j.Slf4j;

@Component("apiDataFetcher")
@Slf4j
public class ApiDataFetcher {

	@Resource(name = "apiDataFetcherMapper")
	private ApiDataFetcherMapper apiDataFetcherMapper;
	
	public MountainWeatherVo getMountainWeatherData(String m_obsid) {
		
		MountainWeatherVo mountainWeatherVo = null;
		
		try {
			
			mountainWeatherVo = apiDataFetcherMapper.getMountainWeatherVo(m_obsid);
			
		} catch (Exception e) {
			
			log.error("DB 실패");
			e.printStackTrace();
		}
		
		return mountainWeatherVo;
	}

	public RiseSetInfoVo getSunsetSunriseData() {
		
		RiseSetInfoVo riseSetInfoVo = null;
		
		try {
			
			riseSetInfoVo = apiDataFetcherMapper.getRiseSetInfoVo();
			
		} catch (Exception e) {
			
			log.error("DB 실패");
			e.printStackTrace();
		}
		
		return riseSetInfoVo;
	}
}
