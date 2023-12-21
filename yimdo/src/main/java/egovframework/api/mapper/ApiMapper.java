package egovframework.api.mapper;

import egovframework.api.vo.ForestPointVo;
import egovframework.api.vo.MountainWeatherVo;
import egovframework.api.vo.RiseSetInfoVo;
import egovframework.api.vo.WthrWrnInfoVo;
import egovframework.serverConfig.annotation.Mapper;

@Mapper("apiMapper")
public interface ApiMapper {

	void insertMountainWeatherData(MountainWeatherVo mountainWeatherVo) throws Exception;
	void insertForestPointData(ForestPointVo forestPointVo) throws Exception;
	void insertRiseSetInfoData(RiseSetInfoVo riseSetInfoVo) throws Exception;
	void insertWthrWrnInfoData(WthrWrnInfoVo wthrWrnInfoVo) throws Exception;
}
