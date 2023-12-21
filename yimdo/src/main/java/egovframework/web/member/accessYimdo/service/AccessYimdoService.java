package egovframework.web.member.accessYimdo.service;

import java.util.List;
import java.util.Map;

import egovframework.web.common.vo.YimdoInfoVo;
import egovframework.web.member.accessYimdo.vo.PurposeEntryVo;
import egovframework.web.member.accessYimdo.vo.RequestEntryYimdoVo;

public interface AccessYimdoService {

	public List<PurposeEntryVo> getPurposeEntryList();
	public List<YimdoInfoVo> getYimdoInfoList();
	public Map<String, Object> insertRequestEntryYimdo(RequestEntryYimdoVo requestEntryYimdoVo);
}
