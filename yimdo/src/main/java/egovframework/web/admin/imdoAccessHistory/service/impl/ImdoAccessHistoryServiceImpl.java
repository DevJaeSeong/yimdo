package egovframework.web.admin.imdoAccessHistory.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.web.admin.imdoAccessHistory.service.ImdoAccessHistoryService;
import egovframework.web.admin.imdoAccessHistory.vo.EntryYimdoVo;
import egovframework.web.common.paging.PagingMaker;
import egovframework.web.common.paging.PagingVo;
import lombok.extern.slf4j.Slf4j;

@Service("imdoAccessHistoryService")
@Slf4j
public class ImdoAccessHistoryServiceImpl implements ImdoAccessHistoryService {

	@Resource(name = "pagingMaker")
	private PagingMaker pagingMaker;
	
	@Resource(name = "imdoAccessHistoryMapper")
	private ImdoAccessHistoryMapper imdoAccessHistoryMapper;

	@Override
	public Map<String, Object> getData(PagingVo pagingVo) {
		log.trace("getData() 시작");
		
		Map<String, Object> map = pagingMaker.pagingMake(pagingVo, getAccessHistoryTotalCount(pagingVo));
		map.put("entryYimdoVos", getAccessHistory((PagingVo) map.get("pagingVo")));
		map.remove("pagingVo");
		
		log.trace("getData() 끝");
		return map;
	}
	
	@Override
	public List<EntryYimdoVo> getAccessHistory(PagingVo pagingVo) {
		log.trace("getAccessHistory() 시작");
		
		List<EntryYimdoVo> entryYimdoVos = null;
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				entryYimdoVos = imdoAccessHistoryMapper.selectAccessHistoryList(pagingVo);
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}
		
		log.trace("getAccessHistory() 끝");
		return entryYimdoVos;
	}

	@Override
	public int getAccessHistoryTotalCount(PagingVo pagingVo) {
		log.trace("getAccessHistoryTotalCount() 시작");

		int result = 0;
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				result = imdoAccessHistoryMapper.selectAccessHistoryListTotalCount(pagingVo);
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}
		
		log.trace("getAccessHistoryTotalCount() 끝");
		return result;
	}
}
