package egovframework.web.admin.managementHistory.service.impl;

import java.util.List;

import egovframework.serverConfig.annotation.Mapper;
import egovframework.web.common.paging.PagingVo;
import egovframework.yimdoSystem.breakerController.BreakerHistoryVo;

@Mapper("breakerHistoryMapper")
public interface BreakerHistoryMapper {

	public List<BreakerHistoryVo> getBreakerHistoryList(PagingVo pagingVo) throws Exception;
	public int getBreakerHistoryListTotalCount(PagingVo pagingVo) throws Exception;
}
