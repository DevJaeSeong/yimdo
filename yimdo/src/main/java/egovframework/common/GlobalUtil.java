package egovframework.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GlobalUtil {
	
	/**
	 * 현재 시각을 문자열로 리턴
	 * 
	 * @return yyyy-MM-dd HH:mm:ss 형태의 문자열
	 */
	public static String getNowDateTime() {
		
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
	
	/**
	 * 현재 날짜를 문자열로 리턴
	 * 
	 * @return yyyyMMdd 형태의 문자열
	 */
	public static String getDateFormat() {
		
		return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
}
