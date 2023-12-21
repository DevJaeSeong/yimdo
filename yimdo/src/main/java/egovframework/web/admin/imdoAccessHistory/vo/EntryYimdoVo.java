package egovframework.web.admin.imdoAccessHistory.vo;

import lombok.Data;

@Data
public class EntryYimdoVo {

	private int requestEntryId;
	private String userId;
	private String userNm;
	private String purposeEntryCode;
	private String purposeEntryName;
	private String yimdoCode;
	private String yimdoName;
	private String expectedEntryDate;
	private String expectedExitDate;
	private String carNum;
	private int entryNum;
	private String regDate;
	private String modDate;
	private String entryDate;
	private String exitDate;
	private int entryHistoryId;
	private String breakerId;
}
