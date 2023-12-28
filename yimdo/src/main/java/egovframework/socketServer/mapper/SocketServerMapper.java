package egovframework.socketServer.mapper;

import java.util.Map;

import egovframework.serverConfig.annotation.Mapper;
import egovframework.socketServer.vo.SocketServerVo;
import egovframework.web.admin.weatherData.vo.WeatherDataVo;
import egovframework.web.common.vo.BreakerInfoVo;

@Mapper("socketServerMapper")
public interface SocketServerMapper {

	void updateBreakerIp(Map<String, String> map) throws Exception;
	void insertPresenceReport(Map<String, String> map) throws Exception;
	void updateBreakerStatus(Map<String, String> map) throws Exception;
	void updateBreakerHistory(Map<String, Object> map) throws Exception;
	void updateBreakerPolicy(Map<String, String> updatePolicyMap) throws Exception;
	String selectBreakerPolicy(String breakerId) throws Exception;
	BreakerInfoVo selectBreakerById(String breakerId) throws Exception;
	void insertWeatherData(WeatherDataVo weatherDataVo) throws Exception;
	void insertBreakerInfo(SocketServerVo socketServerVo) throws Exception;
}
