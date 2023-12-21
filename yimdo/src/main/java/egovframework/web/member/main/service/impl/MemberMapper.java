package egovframework.web.member.main.service.impl;

import egovframework.serverConfig.annotation.Mapper;
import egovframework.web.member.main.vo.MemberVoForCreate;

@Mapper("memberMapper")
public interface MemberMapper {

	public int getCountById(MemberVoForCreate memberVoForCreate) throws Exception;
	public void createAccount(MemberVoForCreate memberVoForCreate) throws Exception;
}
