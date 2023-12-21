package egovframework.web.admin.elementInfo.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.web.admin.elementInfo.service.ElementInfoService;
import lombok.extern.slf4j.Slf4j;

@Service("elementInfoService")
@Slf4j
public class ElementInfoServiceImpl implements ElementInfoService {
	
	@Resource(name = "elementInfoMapper")
	private ElementInfoMapper elementInfoMapper;

	@Override
	public Map<String, Object> updateElement(Map<String, String> msg) {
		log.debug("updateElement() 시작");

		Map<String, Object> map = new HashMap<String, Object>();
		
		int retryCount = 0;
		boolean flag = true;
		while (retryCount < 3 && flag) {
			
			try {
				
				elementInfoMapper.updateElement(msg);
				map.put("result", "success");
				flag = false;
				break;
				
			} catch (Exception e) {
				
				log.error("DB 에러");
				e.printStackTrace();
				retryCount++;
			}
		}

		if (flag)
			map.put("result", "fail");
		
		log.debug("updateElement() 끝");
		return map;
	}

}
