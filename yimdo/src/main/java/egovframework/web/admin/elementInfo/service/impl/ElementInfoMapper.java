package egovframework.web.admin.elementInfo.service.impl;

import java.util.Map;

import egovframework.serverConfig.annotation.Mapper;

@Mapper("elementInfoMapper")
public interface ElementInfoMapper {

	void updateElement(Map<String, String> msg) throws Exception;

}
