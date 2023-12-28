package egovframework.web.admin.managementHistory.service;

import java.util.List;
import java.util.Map;

import egovframework.socketServer.vo.BreakerHistoryVo;
import egovframework.web.common.paging.PagingVo;

public interface BreakerHistoryService {

	Map<String, Object> getData(PagingVo pagingVo);
	List<BreakerHistoryVo> getBreakerHistoryList(PagingVo pagingVo);
	int getBreakerHistoryListTotalCount(PagingVo pagingVo);
}
