package egovframework.yimdoSystem.breakerController;

import egovframework.serverConfig.annotation.Mapper;

@Mapper("breakerControllerMapper")
public interface BreakerControllerMapper {

	public void updateBreakerPolicyDetail(BreakerHistoryVo breakerHistoryVo) throws Exception;
	public void insertBreakerHistory(BreakerHistoryVo breakerHistoryVo) throws Exception;
}
