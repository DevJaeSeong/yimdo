package egovframework.web.admin.imdoAccessHistory.service;

import java.util.List;
import java.util.Map;

import egovframework.web.admin.imdoAccessHistory.vo.EntryYimdoVo;
import egovframework.web.common.paging.PagingVo;

public interface ImdoAccessHistoryService {

	public Map<String, Object> getData(PagingVo pagingVo);
	public List<EntryYimdoVo> getAccessHistory(PagingVo pagingVo);
	public int getAccessHistoryTotalCount(PagingVo pagingVo);
}
