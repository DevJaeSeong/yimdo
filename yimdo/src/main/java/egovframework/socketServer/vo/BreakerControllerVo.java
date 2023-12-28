package egovframework.socketServer.vo;

import lombok.Data;

@Data
public class BreakerControllerVo {

	private String breakerId;
	private String breakerPolicyCode;
	private String modifier;
	private String elementCode;
	private String modifyDetail;
	private String systemControl;
	
	public BreakerControllerVo(String breakerId, String breakerPolicyCode, String modifier, String elementCode, String modifyDetail) {
		
		this.breakerId = breakerId;
		this.breakerPolicyCode = breakerPolicyCode;
		this.modifier = modifier;
		this.elementCode = elementCode;
		this.modifyDetail = modifyDetail;
	}
}
