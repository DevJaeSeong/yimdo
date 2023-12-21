package egovframework.web.admin.breakerStatus.service.impl;

import java.util.Map;

import egovframework.serverConfig.annotation.Mapper;

@Mapper("breakerStatusMapper")
public interface BreakerStatusMapper {

	Map<String, Integer> getBreakerListEachStatusCount(Map<String, String> msgMap) throws Exception;
}
