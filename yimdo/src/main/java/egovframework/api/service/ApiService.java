package egovframework.api.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.api.apies.AbstractApiForm;
import egovframework.api.apies.ForestPointAPI;
import egovframework.api.apies.MountainWeatherAPI;
import egovframework.api.apies.RiseSetInfoAPI;
import egovframework.api.apies.WthrWrnInfoAPI;
import egovframework.api.mapper.ApiMapper;
import egovframework.api.vo.ForestPointVo;
import egovframework.api.vo.MountainWeatherVo;
import egovframework.api.vo.RiseSetInfoVo;
import egovframework.api.vo.WthrWrnInfoVo;
import egovframework.common.GlobalUtil;
import lombok.extern.slf4j.Slf4j;

@Service("apiService")
@Slf4j
public class ApiService extends AbstractApiService {
	
	@Resource(name = "apiMapper")
	private ApiMapper apiMapper;

	@SuppressWarnings("unchecked")
	@Override
	public void insertData(Map<String, Object> data, AbstractApiForm api) {
		
		List<?> vos = (List<?>) data.get("vos");
		String req_date = (String) data.get("req_date");
		
		int result = 0;
		
		if (api instanceof MountainWeatherAPI)
			result = insertMountainWeatherData((List<MountainWeatherVo>) vos, req_date);
		
		else if (api instanceof ForestPointAPI)
			result = insertForestPointData((List<ForestPointVo>) vos, req_date);
		
		else if (api instanceof RiseSetInfoAPI)
			result = insertRiseSetInfoData((List<RiseSetInfoVo>) vos, req_date);
		
		else if (api instanceof WthrWrnInfoAPI)
			result = insertWthrWrnInfoData((List<WthrWrnInfoVo>) vos, req_date);
			
		log.trace("DB에 저장된 데이터 수: {}", result);
	}
	
	private int insertMountainWeatherData(List<MountainWeatherVo> mountainWeatherVos, String req_date) {
		log.trace("데이터 저장 시작");

		int tryMaxCnt, result = 0;

		for (MountainWeatherVo mountainWeatherVo : mountainWeatherVos) {
			log.trace("데이터 저장중");

			tryMaxCnt = 3;
			mountainWeatherVo.setM_req_date(req_date);
			mountainWeatherVo.setM_reg_date(GlobalUtil.getNowDateTime());

			while (tryMaxCnt > 0) {

				tryMaxCnt--;

				try {
					apiMapper.insertMountainWeatherData(mountainWeatherVo);
					result++;
					log.trace("데이터 저장성공");
					break;

				} catch (Exception e) {

					log.warn("데이터 저장실패 남은 시도횟수: {}", tryMaxCnt);
					e.printStackTrace();
					continue;
				}
			}
		}

		log.trace("저장된 데이터 수: {}", result);
		return result;
	}
	
	private int insertForestPointData(List<ForestPointVo> forestPointVos, String req_date) {
		log.trace("데이터 저장 시작");

		int tryMaxCnt, result = 0;

		for (ForestPointVo forestPointVo : forestPointVos) {
			log.trace("데이터 저장중");

			tryMaxCnt = 3;
			forestPointVo.setF_req_date(req_date);
			forestPointVo.setF_reg_date(GlobalUtil.getNowDateTime());

			while (tryMaxCnt > 0) {

				tryMaxCnt--;

				try {
					apiMapper.insertForestPointData(forestPointVo);
					result++;
					log.trace("데이터 저장성공");
					break;

				} catch (Exception e) {

					log.warn("데이터 저장실패 남은 시도횟수: {}", tryMaxCnt);
					e.printStackTrace();
					continue;
				}
			}
		}

		log.trace("저장된 데이터 수: {}", result);
		return result;
	}
	
	private int insertRiseSetInfoData(List<RiseSetInfoVo> riseSetInfoVos, String req_date) {
		log.trace("데이터 저장 시작");

		int tryMaxCnt, result = 0;

		for (RiseSetInfoVo riseSetInfoVo : riseSetInfoVos) {
			log.trace("데이터 저장중");

			tryMaxCnt = 3;
			riseSetInfoVo.setR_req_date(req_date);
			riseSetInfoVo.setR_reg_date(GlobalUtil.getNowDateTime());

			while (tryMaxCnt > 0) {

				tryMaxCnt--;

				try {
					apiMapper.insertRiseSetInfoData(riseSetInfoVo);
					result++;
					log.trace("데이터 저장성공");
					break;

				} catch (Exception e) {

					log.warn("데이터 저장실패 남은 시도횟수: {}", tryMaxCnt);
					e.printStackTrace();
					continue;
				}
			}
		}

		log.trace("저장된 데이터 수: {}", result);
		return result;
	}
	
	private int insertWthrWrnInfoData(List<WthrWrnInfoVo> wthrWrnInfoVos, String req_date) {
		log.trace("데이터 저장 시작");

		int tryMaxCnt, result = 0;

		for (WthrWrnInfoVo wthrWrnInfoVo : wthrWrnInfoVos) {
			log.trace("데이터 저장중");

			tryMaxCnt = 3;
			wthrWrnInfoVo.setW_req_date(req_date);
			wthrWrnInfoVo.setW_reg_date(GlobalUtil.getNowDateTime());

			while (tryMaxCnt > 0) {

				tryMaxCnt--;

				try {
					apiMapper.insertWthrWrnInfoData(wthrWrnInfoVo);
					result++;
					log.trace("데이터 저장성공");
					break;

				} catch (Exception e) {

					log.warn("데이터 저장실패 남은 시도횟수: {}", tryMaxCnt);
					e.printStackTrace();
					continue;
				}
			}
		}

		log.trace("저장된 데이터 수: {}", result);
		return result;
	}
}
