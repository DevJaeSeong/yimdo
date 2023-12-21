package egovframework.yimdoSystem.apiDataFetcher;

import egovframework.api.vo.MountainWeatherVo;
import egovframework.api.vo.RiseSetInfoVo;
import egovframework.serverConfig.annotation.Mapper;

@Mapper("apiDataFetcherMapper")
public interface ApiDataFetcherMapper {

	public MountainWeatherVo getMountainWeatherVo(String m_obsid) throws Exception;
	public RiseSetInfoVo getRiseSetInfoVo() throws Exception;
}
