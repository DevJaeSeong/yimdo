package egovframework.web.admin.breakerManagement.service;

import java.util.Map;

import egovframework.web.admin.breakerManagement.vo.BreakerManagementVo;

public interface BreakerManagementService {

	public Map<String, Object> updateBreakerStatus(BreakerManagementVo breakerManagementVo);
}
