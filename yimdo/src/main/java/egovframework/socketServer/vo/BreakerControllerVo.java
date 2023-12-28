package egovframework.socketServer.vo;

import lombok.Data;

/**
 * 차단기 상태변경 요청을 전송하는데 사용되는 Vo
 * <hr>
 * <p>
 * 	private String breakerId			- 상태를 변경할 차단기ID<br>
 *	private String breakerPolicyCode	- 차단기에 적용할 정책<br>
 *	private String modifier				- 차단기 상태를 변경하려는 주체<br>
 *	private String elementCode			- 상태변경인자 코드<br>
 *	private String modifyDetail			- 상태변경 상세사유<br>
 *	private String systemControl		- 시스템이 제어중인지 여부
 * </p>
 * <hr>
 */
@Data
public class BreakerControllerVo {

	private String breakerId;			// 상태를 변경할 차단기ID
	private String breakerPolicyCode;	// 차단기에 적용할 정책
	private String modifier;			// 차단기 상태를 변경하려는 주체
	private String elementCode;			// 상태변경인자 코드
	private String modifyDetail;		// 상태변경 상세사유
	private String systemControl;		// 시스템이 제어중인지 여부
	
	public BreakerControllerVo(String breakerId, String breakerPolicyCode, String modifier, String elementCode, String modifyDetail) {
		
		this.breakerId = breakerId;
		this.breakerPolicyCode = breakerPolicyCode;
		this.modifier = modifier;
		this.elementCode = elementCode;
		this.modifyDetail = modifyDetail;
	}
}
