package egovframework.web.member.main.vo;

import lombok.Data;

@Data
public class MemberVoForCreate {

	private String userId;
	private String password;
	private String userNm;
	private String mbtlNum;
	private String email;
	private String carNumber;
	private String affiliation;
	private int authorityId = 2;
	private boolean isAccountNonExpired = true;
	private boolean isAccountNonLocked = true;
	private boolean isCredentialsNonExpired = true;
	private boolean isEnabled = true;
}
