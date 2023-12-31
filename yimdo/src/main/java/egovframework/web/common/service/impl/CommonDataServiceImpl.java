package egovframework.web.common.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.web.common.paging.PagingMaker;
import egovframework.web.common.paging.PagingVo;
import egovframework.web.common.service.CommonDataService;
import egovframework.web.common.vo.BreakerInfoVo;
import egovframework.web.common.vo.ModifyElementVo;
import egovframework.web.common.vo.SidoVo;
import egovframework.web.common.vo.SigunguVo;
import egovframework.web.common.vo.YimdoInfoVo;
import lombok.extern.slf4j.Slf4j;

@Service("commonDataService")
@Slf4j
public class CommonDataServiceImpl implements CommonDataService {
	
	@Resource(name = "pagingMaker")
	private PagingMaker pagingMaker;
	
	@Resource(name = "commonDataMapper")
	private CommonDataMapper commonDataMapper;

	@Override
	public List<SidoVo> getSido() {
		log.trace("getSido() 시작");

		List<SidoVo> sidoVos = null;
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				sidoVos = commonDataMapper.getSido();
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}
		
		log.trace("getSido() 끝");
		return sidoVos;
	}

	@Override
	public List<SigunguVo> getSigungu(String sido) {
		log.trace("getSigungu() 시작");

		List<SigunguVo> sigunguVos = null;
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				sigunguVos = commonDataMapper.getSigungu(sido);
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}
		
		log.trace("getSigungu() 끝");
		return sigunguVos;
	}
	
	@Override
	public Map<String, Object> getYimdoData(PagingVo pagingVo) {
		log.trace("getYimdoData() 시작");
		
		Map<String, Object> map = pagingMaker.pagingMake(pagingVo, getYimdoListTotalCount(pagingVo));
		map.put("yimdoInfoVos", getYimdoList((PagingVo) map.get("pagingVo")));
		map.remove("pagingVo");
		
		log.trace("getYimdoData() 끝");
		return map;
	}
	
	@Override
	public List<YimdoInfoVo> getYimdoList(PagingVo pagingVo) {
		log.trace("getYimdoInfoList() 시작");
		
		List<YimdoInfoVo> yimdoInfoVos = null;
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				yimdoInfoVos = commonDataMapper.getYimdoList(pagingVo);
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}
		
		log.trace("getYimdoInfoList() 끝");
		return yimdoInfoVos;
	}

	@Override
	public int getYimdoListTotalCount(PagingVo pagingVo) {
		log.trace("getYimdoInfoList() 시작");

		int result = 0;
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				result = commonDataMapper.getYimdoListTotalCount(pagingVo);
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}
		
		log.trace("getYimdoInfoList() 끝");
		return result;
	}

	@Override
	public Map<String, Object> getBreakerData(PagingVo pagingVo) {
		log.trace("getBreakerData() 시작");
		
		Map<String, Object> map = pagingMaker.pagingMake(pagingVo, getBreakerListTotalCount(pagingVo));
		map.put("breakerInfoVos", getBreakerList((PagingVo) map.get("pagingVo")));
		map.remove("pagingVo");
		
		log.trace("getBreakerData() 끝");
		return map;
	}
	
	@Override
	public List<BreakerInfoVo> getBreakerList(PagingVo pagingVo) {
		log.trace("getBreakerList() 시작");
		
		List<BreakerInfoVo> breakerInfoVos = null;
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				breakerInfoVos = commonDataMapper.getBreakerList(pagingVo);
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}
		
		log.trace("getBreakerList() 끝");
		return breakerInfoVos;
	}

	@Override
	public int getBreakerListTotalCount(PagingVo pagingVo) {
		log.trace("getBreakerListTotalCount() 시작");

		int result = 0;
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				result = commonDataMapper.getBreakerListTotalCount(pagingVo);
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}
		
		log.trace("getBreakerListTotalCount() 끝");
		return result;
	}
	
	@Override
	public Map<String, Object> getElementData(PagingVo pagingVo) {
		log.trace("getElementData() 시작");
		
		Map<String, Object> map = pagingMaker.pagingMake(pagingVo, getModifyElementTotalCnt(pagingVo));
		map.put("modifyElementVos", getModifyElementList((PagingVo) map.get("pagingVo")));
		map.remove("pagingVo");
		
		log.trace("getElementData() 끝");
		return map;
	}
	
	@Override
	public List<ModifyElementVo> getModifyElementList(PagingVo pagingVo) {
		log.trace("getModifyElementList() 시작");
		
		List<ModifyElementVo> modifyElementVos = null;
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				modifyElementVos = commonDataMapper.getModifyElementList(pagingVo);
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}
		
		log.trace("getModifyElementList() 끝");
		return modifyElementVos;
	}

	@Override
	public int getModifyElementTotalCnt(PagingVo pagingVo) {
		log.trace("getModifyElementTotalCnt() 시작");
		
		int totalCnt = -1;
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				totalCnt = commonDataMapper.getModifyElementTotalCnt(pagingVo);
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}
		
		log.trace("getModifyElementTotalCnt() 끝");
		return totalCnt;
	}
}
