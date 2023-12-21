package egovframework.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import egovframework.serverConfig.ServerConfig;

public class CookieUtil {

	/**
	 * 원하는 쿠키 삭제 메서드.
	 * 
	 * @param cookieName 삭제할 쿠키 이름
	 * @param request
	 * @param response
	 */
    public static void deleteCookie(String cookieName, HttpServletRequest request, HttpServletResponse response) {
    	
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
        	
            for (Cookie cookie : cookies) {
            	
                if (cookie.getName().equals(cookieName)) {
                	
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }
    
    /**
     * 원하는 쿠키 삭제 메서드.
     * 
     * @param cookieName 삭제할 쿠키 이름
     * @param request
     * @param response
     */
    public static void deleteIdentifyToken(HttpServletRequest request, HttpServletResponse response) {
    	
		Cookie cookie = new Cookie(ServerConfig.IDENTIFY_TOKEN_NAME, null);
		cookie.setDomain(request.getServerName());
		cookie.setPath("/");
		cookie.setSecure(false);
		cookie.setMaxAge(0);
		
		response.addCookie(cookie);
    }
}
