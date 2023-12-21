package egovframework.web.member.accessYimdo.vo;

import lombok.Data;

@Data
public class RequestEntryYimdoVo {

	private String userId;
	private String purposeEntryCode;
	private String yimdoCode;
	private String expectedEntryDate;
	private String expectedExitDate;
	private String carNum;
	private int entryNum;
	private String regDate;
	private String modDate;
}
