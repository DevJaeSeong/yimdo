package egovframework.web.common.service;

import java.util.List;
import java.util.Map;

import egovframework.web.common.paging.PagingVo;
import egovframework.web.common.vo.BreakerInfoVo;
import egovframework.web.common.vo.ModifyElementVo;
import egovframework.web.common.vo.SidoVo;
import egovframework.web.common.vo.SigunguVo;
import egovframework.web.common.vo.YimdoInfoVo;

public interface CommonDataService {

	public List<SidoVo> getSido();
	public List<SigunguVo> getSigungu(String sido);
	
	public Map<String, Object> getYimdoData(PagingVo pagingVo);
	public List<YimdoInfoVo> getYimdoList(PagingVo pagingVo);
	public int getYimdoListTotalCount(PagingVo pagingVo);
	
	public Map<String, Object> getBreakerData(PagingVo pagingVo);
	public List<BreakerInfoVo> getBreakerList(PagingVo pagingVo);
	public int getBreakerListTotalCount(PagingVo pagingVo);
	
	public Map<String, Object> getElementData(PagingVo pagingVo);
	public List<ModifyElementVo> getModifyElementList(PagingVo pagingVo);
	public int getModifyElementTotalCnt(PagingVo pagingVo);
}
