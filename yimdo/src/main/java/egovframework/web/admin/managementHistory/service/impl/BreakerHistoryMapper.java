package egovframework.web.admin.managementHistory.service.impl;

import java.util.List;

import egovframework.serverConfig.annotation.Mapper;
import egovframework.socketServer.vo.BreakerHistoryVo;
import egovframework.web.common.paging.PagingVo;

@Mapper("breakerHistoryMapper")
public interface BreakerHistoryMapper {

	public List<BreakerHistoryVo> getBreakerHistoryList(PagingVo pagingVo) throws Exception;
	public int getBreakerHistoryListTotalCount(PagingVo pagingVo) throws Exception;
}
