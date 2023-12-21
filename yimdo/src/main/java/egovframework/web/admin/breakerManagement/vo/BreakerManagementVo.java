package egovframework.web.admin.breakerManagement.vo;

import java.util.List;

import lombok.Data;

@Data
public class BreakerManagementVo {

	private List<String> selectedBreakers;
	private String selectedElement;
	private String selectedPolicy;
	private String modifyDetail;
	private String modifier;
	private String systemControl;
}
