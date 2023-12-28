package egovframework.socketServer.mapper;

import egovframework.serverConfig.annotation.Mapper;
import egovframework.socketServer.vo.BreakerHistoryVo;

@Mapper("breakerControllerMapper")
public interface BreakerControllerMapper {

	public void updateBreakerPolicyDetail(BreakerHistoryVo breakerHistoryVo) throws Exception;
	public void insertBreakerHistory(BreakerHistoryVo breakerHistoryVo) throws Exception;
	public String selectBreakerStatusById(String breakerId) throws Exception;
}
