package egovframework.web.admin.imdoAccessHistory.service.impl;

import java.util.List;

import egovframework.serverConfig.annotation.Mapper;
import egovframework.web.admin.imdoAccessHistory.vo.EntryYimdoVo;
import egovframework.web.common.paging.PagingVo;

@Mapper("imdoAccessHistoryMapper")
public interface ImdoAccessHistoryMapper {

	public EntryYimdoVo selectRequestAccessYimdo() throws Exception;
	public void insertAccessHistory(EntryYimdoVo entryYimdoVo) throws Exception;
	public void updateRequestAccessYimdoEntry() throws Exception;
	public void updateRequestAccessYimdoExit() throws Exception;
	public List<EntryYimdoVo> selectAccessHistoryList(PagingVo pagingVo) throws Exception;
	public int selectAccessHistoryListTotalCount(PagingVo pagingVo) throws Exception;

}
