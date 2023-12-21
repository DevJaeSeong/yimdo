package egovframework.api.vo;

import lombok.Data;

@Data
public class RiseSetInfoVo implements BreakerControlElement {
	
	private int r_no;
	private String r_aste;
	private String r_astm;
	private String r_civile;
	private String r_civilm;
	private String r_latitude;
	private String r_latitudeNum;
	private String r_location;
	private String r_locdate;
	private String r_longitude;
	private String r_longitudeNum;
	private String r_moonrise;
	private String r_moonset;
	private String r_moontransit;
	private String r_naute;
	private String r_nautm;
	private String r_sunrise;
	private String r_sunset;
	private String r_suntransit;
	private String r_req_date;
	private String r_reg_date;
	
}
