package egovframework.web.admin.managementHistory.service;

import java.util.List;
import java.util.Map;

import egovframework.web.common.paging.PagingVo;
import egovframework.yimdoSystem.breakerController.BreakerHistoryVo;

public interface BreakerHistoryService {

	Map<String, Object> getData(PagingVo pagingVo);
	List<BreakerHistoryVo> getBreakerHistoryList(PagingVo pagingVo);
	int getBreakerHistoryListTotalCount(PagingVo pagingVo);
}
