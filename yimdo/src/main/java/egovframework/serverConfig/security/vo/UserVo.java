package egovframework.serverConfig.security.vo;

import javax.validation.constraints.Pattern;

import lombok.Data;

/**
 * 이 클래스는 사용자 인증시 필요한 사용되는 유저 정보를 가지고 있는 Vo.<br>
 * 인증, 인가에 필요한 필수 필드를 보유하고 있으니 수정에 주의.
 */
@Data
public class UserVo {
	
	@Pattern(regexp = "[a-zA-Z0-9]+", message = "영문자 또는 숫자만 허용됩니다.")
	private String userId;
	
	private String password;
	private int authorityId;
	private boolean isAccountNonExpired;
	private boolean isAccountNonLocked;
	private boolean isCredentialsNonExpired;
	private boolean isEnabled;
}
