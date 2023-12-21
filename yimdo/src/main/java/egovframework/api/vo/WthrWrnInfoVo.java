package egovframework.api.vo;

import lombok.Data;

@Data
public class WthrWrnInfoVo implements BreakerControlElement {

	private int w_no;
	private String w_allEndTime;
	private String w_areaCode;
	private String w_areaName;
	private String w_cancel;
	private String w_command;
	private String w_endTime;
	private String w_stnId;
	private String w_tmFc;
	private String w_tmSeq;
	private String w_warnVar;
	private String w_warnStress;
	private String w_startTime;
	private String w_req_date;
	private String w_reg_date;
	
}
