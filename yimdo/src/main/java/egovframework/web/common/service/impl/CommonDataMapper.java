package egovframework.web.common.service.impl;

import java.util.List;

import egovframework.serverConfig.annotation.Mapper;
import egovframework.web.common.paging.PagingVo;
import egovframework.web.common.vo.BreakerInfoVo;
import egovframework.web.common.vo.ModifyElementVo;
import egovframework.web.common.vo.SidoVo;
import egovframework.web.common.vo.SigunguVo;
import egovframework.web.common.vo.YimdoInfoVo;

@Mapper("commonDataMapper")
public interface CommonDataMapper {

	public List<SidoVo> getSido() throws Exception;
	public List<SigunguVo> getSigungu(String sido) throws Exception;
	
	public List<YimdoInfoVo> getYimdoList(PagingVo pagingVo) throws Exception;
	public int getYimdoListTotalCount(PagingVo pagingVo) throws Exception;
	
	public BreakerInfoVo getBreakerById(String breakerId) throws Exception;
	public List<BreakerInfoVo> getAllBreakerList() throws Exception;
	public List<BreakerInfoVo> getBreakerList(PagingVo pagingVo) throws Exception;
	public int getBreakerListTotalCount(PagingVo pagingVo) throws Exception;
	
	public List<ModifyElementVo> getUsedModifyElementList() throws Exception;
	public List<ModifyElementVo> getModifyElementList(PagingVo pagingVo) throws Exception;
	public int getModifyElementTotalCnt(PagingVo pagingVo) throws Exception;
}
