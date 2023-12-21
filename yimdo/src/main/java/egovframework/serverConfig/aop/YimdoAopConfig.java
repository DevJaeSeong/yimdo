package egovframework.serverConfig.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AOP 활성화 클래스.
 */
@Configuration
@EnableAspectJAutoProxy
public class YimdoAopConfig {

	@Bean
	public YimdoAspect yimdoAspect() {
		
		return new YimdoAspect();
	}
}
