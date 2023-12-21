package egovframework.web.admin.weatherData.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.web.admin.weatherData.service.WeatherDataService;
import egovframework.web.admin.weatherData.vo.WeatherDataSearchVo;
import egovframework.web.admin.weatherData.vo.WeatherDataVo;

@Controller
@RequestMapping("/admin/weatherData")
public class WeatherDataController {

	@Resource(name = "weatherDataService")
	private WeatherDataService weatherDataService;
	
	@GetMapping("/weatherDataPage.do")
	public String weatherDataPage() {
		
		return "admin/weatherData";
	}
	
	@GetMapping("/getWeatherData.do")
	@ResponseBody
	public Object getWeatherData(@ModelAttribute WeatherDataSearchVo weatherDataSearchVo) {
		
		List<WeatherDataVo> weatherDataVos = weatherDataService.getWeatherData(weatherDataSearchVo);
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("week", weatherDataSearchVo.getWeek());
		data.put("weatherDataVos", weatherDataVos);
		
		return data;
	}
}
