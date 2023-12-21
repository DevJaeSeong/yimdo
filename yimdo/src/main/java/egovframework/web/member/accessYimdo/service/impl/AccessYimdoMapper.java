package egovframework.web.member.accessYimdo.service.impl;

import java.util.List;

import egovframework.serverConfig.annotation.Mapper;
import egovframework.web.common.vo.YimdoInfoVo;
import egovframework.web.member.accessYimdo.vo.PurposeEntryVo;
import egovframework.web.member.accessYimdo.vo.RequestEntryYimdoVo;

@Mapper("accessYimdoMapper")
public interface AccessYimdoMapper {

	List<PurposeEntryVo> getPurposeEntryList() throws Exception;
	List<YimdoInfoVo> getYimdoInfoList() throws Exception;
	int insertRequestEntryYimdo(RequestEntryYimdoVo requestEntryYimdoVo) throws Exception;

}
