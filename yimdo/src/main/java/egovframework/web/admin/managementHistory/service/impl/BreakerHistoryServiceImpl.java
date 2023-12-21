package egovframework.web.admin.managementHistory.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.web.admin.managementHistory.service.BreakerHistoryService;
import egovframework.web.common.paging.PagingMaker;
import egovframework.web.common.paging.PagingVo;
import egovframework.yimdoSystem.breakerController.BreakerHistoryVo;
import lombok.extern.slf4j.Slf4j;

@Service("breakerHistoryService")
@Slf4j
public class BreakerHistoryServiceImpl implements BreakerHistoryService {
	
	@Resource(name = "pagingMaker")
	private PagingMaker pagingMaker;
	
	@Resource(name = "breakerHistoryMapper")
	private BreakerHistoryMapper breakerHistoryMapper;

	@Override
	public Map<String, Object> getData(PagingVo pagingVo) {
		log.trace("getData() 시작");
		
		Map<String, Object> map = pagingMaker.pagingMake(pagingVo, getBreakerHistoryListTotalCount(pagingVo));
		map.put("breakerHistoryVos", getBreakerHistoryList((PagingVo) map.get("pagingVo")));
		map.remove("pagingVo");
		
		log.trace("getData() 끝");
		return map;
	}
	
	@Override
	public List<BreakerHistoryVo> getBreakerHistoryList(PagingVo pagingVo) {
		log.trace("getBreakerHistoryList() 시작");
		
		List<BreakerHistoryVo> breakerHistoryVos = null;
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				breakerHistoryVos = breakerHistoryMapper.getBreakerHistoryList(pagingVo);
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}
		
		log.trace("getBreakerHistoryList() 끝");
		return breakerHistoryVos;
	}

	@Override
	public int getBreakerHistoryListTotalCount(PagingVo pagingVo) {
		log.trace("getBreakerHistoryListTotalCount() 시작");

		int result = 0;
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				result = breakerHistoryMapper.getBreakerHistoryListTotalCount(pagingVo);
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}
		
		log.trace("getBreakerHistoryListTotalCount() 끝");
		return result;
	}
}
