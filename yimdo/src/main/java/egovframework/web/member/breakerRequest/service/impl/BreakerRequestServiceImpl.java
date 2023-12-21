package egovframework.web.member.breakerRequest.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.serverConfig.ServerConfig;
import egovframework.web.admin.imdoAccessHistory.service.impl.ImdoAccessHistoryMapper;
import egovframework.web.admin.imdoAccessHistory.vo.EntryYimdoVo;
import egovframework.web.common.service.impl.CommonDataMapper;
import egovframework.web.common.vo.BreakerInfoVo;
import egovframework.web.member.breakerRequest.service.BreakerRequestService;
import egovframework.yimdoSystem.breakerController.BreakerController;
import lombok.extern.slf4j.Slf4j;

@Service("breakerRequestService")
@Slf4j
public class BreakerRequestServiceImpl implements BreakerRequestService {
	
	@Resource(name = "commonDataMapper")
	private CommonDataMapper commonDataMapper;
	
	@Resource(name = "breakerController")
	private BreakerController breakerController;
	
	@Resource(name = "imdoAccessHistoryMapper")
	private ImdoAccessHistoryMapper imdoAccessHistoryMapper;
	
	@Override
	public Map<String, Object> clientRequestNormalOpen(Map<String, String> msgMap) {
		log.debug("breakerRequest() 시작");
		
		String breakerId = msgMap.get("breakerId");
		String userNm = msgMap.get("userNm");
		int result = 0;
		
		try {
			
			BreakerInfoVo breakerInfoVo = commonDataMapper.getBreakerById(breakerId);
			
			breakerInfoVo.setBreakerPolicyCode(ServerConfig.breakerPolicyNormalOpen);
			breakerInfoVo.setElementCode("0001");
			breakerInfoVo.setModifier(userNm);
			breakerInfoVo.setModifyDetail(userNm + " 사용자의 요청에 의한 정상개방.");
			
			result = breakerController.breakerRequest(breakerInfoVo);
			
		} catch (Exception e) {
			
			log.error("DB에러");
			e.printStackTrace();
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (result > 0) {
			
			map.put("result", "success");
			//registAccessHistory();	//임도출입이력 전용(추후 변경되거나 삭제)

		} else {
			
			map.put("result", "fail");
		}
		
		log.debug("breakerRequest() 끝");
		return map;
	}
	
	@SuppressWarnings("unused")
	private void registAccessHistory() {
		
		try {
			
			EntryYimdoVo entryYimdoVo = imdoAccessHistoryMapper.selectRequestAccessYimdo();
			
			if (entryYimdoVo.getEntryDate() == null) {
				
				imdoAccessHistoryMapper.updateRequestAccessYimdoEntry();
				
			} else {
				
				imdoAccessHistoryMapper.updateRequestAccessYimdoExit();
			}
			
			entryYimdoVo = imdoAccessHistoryMapper.selectRequestAccessYimdo();
			entryYimdoVo.setBreakerId("5001");
			
			imdoAccessHistoryMapper.insertAccessHistory(entryYimdoVo);
			
		} catch (Exception e) {

			log.error("입도출입이력 에러");
			e.printStackTrace();
		}
	}
}
