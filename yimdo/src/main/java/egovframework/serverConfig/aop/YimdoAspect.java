package egovframework.serverConfig.aop;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Component;

/**
 * AOP 설정 클래스.
 */
@Component
@Aspect
public class YimdoAspect {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 *  스프링 시큐리티 FilterChainProxy의 doFilter() 메서드를 타겟으로 지정.
	 *  
	 * @param proceedingJoinPoint
	 * @throws Throwable
	 */
	@Around("execution(* org.springframework.security.web.FilterChainProxy.doFilter(..))")
    public void requestRejectedExceptionHandle (ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
        try {
        	
        	proceedingJoinPoint.proceed();
            
        } catch (RequestRejectedException e) {
        	
			log.error("접근거부: {}", e.getMessage());
			
			HttpServletResponse response = (HttpServletResponse) proceedingJoinPoint.getArgs()[1];
			
			response.sendRedirect("/general/home.do");
        }
    }
}
