package egovframework.serverConfig.security.mapper;

import egovframework.serverConfig.annotation.Mapper;
import egovframework.serverConfig.security.vo.UserDetailVo;
import egovframework.serverConfig.security.vo.UserVo;

@Mapper("securityMapper")
public interface SecurityMapper {

	public UserVo getUserByIdForLogin(String userId) throws Exception;
	public UserDetailVo getUserDetailVoById(String userId) throws Exception;
}
