package egovframework.web.admin.weatherData.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.web.admin.weatherData.mapper.WeatherDataMapper;
import egovframework.web.admin.weatherData.vo.WeatherDataSearchVo;
import egovframework.web.admin.weatherData.vo.WeatherDataVo;

@Service("weatherDataService")
public class WeatherDataServiceImpl implements WeatherDataService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "weatherDataMapper")
	private WeatherDataMapper WeatherDataMapper;

	@Override
	public List<WeatherDataVo> getWeatherData(WeatherDataSearchVo weatherDataSearchVo) {
		
		List<WeatherDataVo> weatherDataVos = null;
		
		try {
			
			weatherDataVos = WeatherDataMapper.getWeatherData(weatherDataSearchVo);
			
		} catch (Exception e) {
			
			log.error("DB 에러");
			e.printStackTrace();
		}
		
		return weatherDataVos;
	}
}
