package egovframework.api.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import egovframework.api.apies.ForestPointAPI;
import egovframework.api.apies.MountainWeatherAPI;
import egovframework.api.apies.RiseSetInfoAPI;
import egovframework.api.apies.WthrWrnInfoAPI;
import egovframework.api.service.ApiService;

@Controller
@SuppressWarnings("unused")
public class ApiController {
	
	@Resource(name = "apiService")
	private ApiService apiService;
	
	@Resource(name = "mountainWeatherAPI")
	private MountainWeatherAPI mountainWeatherAPI;
	
	@Resource(name = "forestPointAPI")
	private ForestPointAPI forestPointAPI;
	
	@Resource(name = "riseSetInfoAPI")
	private RiseSetInfoAPI riseSetInfoAPI;
	
	@Resource(name = "wthrWrnInfoAPI")
	private WthrWrnInfoAPI wthrWrnInfoAPI;
	
	/**
	 * 산악기상정보 API(Mtweather)
	 */
	//@Scheduled(cron = "0 */2 * * * *")
	private void callMountainWeatherAPI() {
		
		apiService.doDataProccessing(mountainWeatherAPI);
	}
	
	/**
	 * 기상특보 조회서비스 API(WthrWrnInfoService)
	 */
	//@Scheduled(cron = "0 */10 * * * *")
	private void callWthrWrnInfoAPI() {
		
		apiService.doDataProccessing(wthrWrnInfoAPI);
	}
	
	/**
	 * 산불위험예보정보 서비스API(ForestPoint)
	 */
	//@Scheduled(cron = "0 0 */1 * * *")
	private void callForestPointAPI() {
		
		apiService.doDataProccessing(forestPointAPI);
	}
	
	/**
	 * 출몰시각 정보제공 서비스 API(RiseSetInfoService)
	 */
	//@Scheduled(cron = "5 0 0 * * *")
	private void callRiseSetInfoAPI() {
		
		apiService.doDataProccessing(riseSetInfoAPI);
	}
}
