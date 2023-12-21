package egovframework.web.member.accessYimdo.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.web.common.vo.YimdoInfoVo;
import egovframework.web.member.accessYimdo.service.AccessYimdoService;
import egovframework.web.member.accessYimdo.vo.PurposeEntryVo;
import egovframework.web.member.accessYimdo.vo.RequestEntryYimdoVo;

@Service("accessYimdoService")
public class AccessYimdoServiceImpl implements AccessYimdoService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "accessYimdoMapper")
	private AccessYimdoMapper accessYimdoMapper;

	@Override
	public List<PurposeEntryVo> getPurposeEntryList() {
		log.debug("getpurposeEntryList() 시작");

		List<PurposeEntryVo> purposeEntryVos = null;
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				purposeEntryVos = accessYimdoMapper.getPurposeEntryList();
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}
		
		log.debug("getpurposeEntryList() 끝");
		return purposeEntryVos;
	}

	@Override
	public List<YimdoInfoVo> getYimdoInfoList() {
		log.debug("getYimdoInfoList() 시작");

		List<YimdoInfoVo> yimdoInfoVos = null;
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				yimdoInfoVos = accessYimdoMapper.getYimdoInfoList();
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}
		
		log.debug("getYimdoInfoList() 끝");
		return yimdoInfoVos;
	}

	@Override
	public Map<String, Object> insertRequestEntryYimdo(RequestEntryYimdoVo requestEntryYimdoVo) {
		log.debug("getYimdoInfoList() 시작");

		Map<String, Object> map = new HashMap<String, Object>();
		
		int retryCount = 0;
		while (retryCount < 3) {
			
			try {
				
				int result = accessYimdoMapper.insertRequestEntryYimdo(requestEntryYimdoVo);
				if (result > 0) map.put("result", "success");
				else map.put("result", "fail");
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
			
			if (retryCount >= 3) map.put("result", "fail");
		}
		
		log.debug("getYimdoInfoList() 끝");
		return map;
	}
	
}
