package egovframework.web.admin.weatherData.service;

import java.util.List;

import egovframework.web.admin.weatherData.vo.WeatherDataSearchVo;
import egovframework.web.admin.weatherData.vo.WeatherDataVo;

public interface WeatherDataService {

	public List<WeatherDataVo> getWeatherData(WeatherDataSearchVo weatherDataSearchVo);

}
