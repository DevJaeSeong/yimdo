package egovframework.web.admin.weatherData.mapper;

import java.util.List;

import egovframework.serverConfig.annotation.Mapper;
import egovframework.web.admin.weatherData.vo.WeatherDataSearchVo;
import egovframework.web.admin.weatherData.vo.WeatherDataVo;

@Mapper("weatherDataMapper")
public interface WeatherDataMapper {

	public List<WeatherDataVo> getWeatherData(WeatherDataSearchVo weatherDataSearchVo) throws Exception;

}
