package egovframework.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    /**
     * 사용자에게 쿠키 전송.
     * 
     * @param cookieName 전달할 쿠키 이름
     * @param value 전달할 쿠키 값
     * @param request
     * @param response
     */
    public static void sendCookie(String cookieName, String value, HttpServletRequest request, HttpServletResponse response) {
    
		Cookie cookie = new Cookie(cookieName, value);
		cookie.setDomain(request.getServerName());
		cookie.setPath("/");
		cookie.setSecure(false);
		cookie.setHttpOnly(true);
		
		response.addCookie(cookie);
    }
	
    /**
     * 사용자의 쿠키 가져오기.
     * 
     * @param cookieName 가져올 쿠키 이름
     * @param request
     * @return
     */
    public static Cookie getCookie(String cookieName, HttpServletRequest request) {
    	
    	Cookie wantedCookie = null;
    	Cookie[] cookies = request.getCookies();
    	
		for (Cookie cookie : cookies) {
			
			if (cookieName.equals(cookie.getName())) {
				
				wantedCookie = cookie;
				break;
			}
		}
    	
    	return wantedCookie;
    }
    
    /**
     * 사용자의 쿠키 삭제.
     * 
     * @param cookieName 삭제할 쿠키 이름
     * @param request
     * @param response
     */
    public static void deleteCookie(String cookieName, HttpServletRequest request, HttpServletResponse response) {
    	
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setDomain(request.getServerName());
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(false);
		cookie.setMaxAge(0);
		
		response.addCookie(cookie);
    }
}
