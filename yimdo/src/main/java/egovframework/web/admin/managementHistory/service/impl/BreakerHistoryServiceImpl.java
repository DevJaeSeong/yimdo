package egovframework.web.admin.managementHistory.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.socketServer.vo.BreakerHistoryVo;
import egovframework.web.admin.managementHistory.service.BreakerHistoryService;
import egovframework.web.common.paging.PagingMaker;
import egovframework.web.common.paging.PagingVo;
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
		
		List<BreakerHistoryVo> breakerHistoryVos = null;
		
		try {
			
			breakerHistoryVos = breakerHistoryMapper.getBreakerHistoryList(pagingVo);
			
		} catch (Exception e) {
			
			log.error("DB 에러");
			e.printStackTrace();
		}
		
		return breakerHistoryVos;
	}

	@Override
	public int getBreakerHistoryListTotalCount(PagingVo pagingVo) {

		int result = 0;
		
		try {
			
			result = breakerHistoryMapper.getBreakerHistoryListTotalCount(pagingVo);
			
		} catch (Exception e) {
			
			log.error("DB 에러");
			e.printStackTrace();
		}
		
		return result;
	}
}
