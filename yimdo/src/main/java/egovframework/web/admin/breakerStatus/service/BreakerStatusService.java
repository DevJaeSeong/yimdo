package egovframework.web.admin.breakerStatus.service;

import java.util.Map;

import egovframework.web.admin.breakerStatus.vo.BreakerManagementVo;

public interface BreakerStatusService {

	public Map<String, Integer> getBreakerListEachStatusCount(Map<String, String> msgMap);
	public void updateBreakerStatus(BreakerManagementVo breakerManagementVo);
}
