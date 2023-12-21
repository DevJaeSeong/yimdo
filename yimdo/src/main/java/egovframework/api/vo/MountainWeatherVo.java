package egovframework.api.vo;

import lombok.Data;

@Data
public class MountainWeatherVo implements BreakerControlElement {

	private int m_no;
	private String m_localarea;			// 지역코드
	private String m_obsid;				// 지점번호
	private String m_obsname;			// 산이름
	private String m_tm;				// 관측시간
	private String m_cprn;				// 무게식 강우량
	private String m_rn;				// 전도식 강우량
	private String m_pa;				// 기압
	private String m_ts;				// 지면온도
	private String m_hm10m;				// 10m 습도
	private String m_hm2m;				// 2m 습도
	private String m_tm10m;				// 10m 기온
	private String m_tm2m;				// 2m 기온
	private String m_wd10m;				// 10m 풍향
	private String m_wd10mstr;			// 10m 풍향방위
	private String m_wd2m;				// 2m 풍향
	private String m_wd2mstr;			// 2m 풍향방위
	private String m_ws10m;				// 10m 풍속
	private String m_ws2m;				// 2m 풍속
	private String m_req_date;			// 요청시각
	private String m_reg_date;			// 등록시각
	
}
