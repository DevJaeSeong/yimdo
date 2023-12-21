package egovframework.serverConfig.mvc;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import egovframework.serverConfig.interceptor.YimdoInterceptor;

/**
 * spring web MVC 설정하는 클래스.
 */
@Configuration
@EnableWebMvc
public class YimdoWebMvcConfigurer implements WebMvcConfigurer {
	
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {

	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {

	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {

	}

	@Override
	public void addFormatters(FormatterRegistry registry) {

	}

	/**
	 * 인터셉터 추가.
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(new YimdoInterceptor()).addPathPatterns("/**");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {

	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {

	}

	/**
	 * 뷰 리졸버 구현.
	 */
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {

		UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
		
		urlBasedViewResolver.setOrder(1);
		urlBasedViewResolver.setViewClass(JstlView.class);
		urlBasedViewResolver.setPrefix("/WEB-INF/jsp/");
		urlBasedViewResolver.setSuffix(".jsp");
		
		registry.viewResolver(urlBasedViewResolver);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {

	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

	}

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {

	}

	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {

	}

	/**
	 * hibernate validator 사용.
	 */
	@Override
	public Validator getValidator() {
		
        return new LocalValidatorFactoryBean();
	}

	@Override
	public MessageCodesResolver getMessageCodesResolver() {
		
		return null;
	}
	
	/*
	 * RequestMappingHandlerAdapter : HTTP 요청을 처리하고 컨트롤러 메서드를 호출하는 주요 컴포넌트.
	 * 
	 * 기본적으로 아래 컨버터가 추가되어 별도 설정없이 json, from 같은 데이터를 처리가능함.
	 * 
	 * 1. ByteArrayHttpMessageConverter				: 바이너리 데이터 처리 (이미지 파일이나 다른 바이너리 데이터)
	 * 2. StringHttpMessageConverter				: 문자열 데이터를 처리 (HTML form 데이터, 텍스트 메시지, 문자열 데이터)
	 * 3. ResourceHttpMessageConverter				: 파일 다운로드 또는 업로드와 관련된 요청을 처리 (클래스패스나 파일 시스템의 리소스)
	 * 4. SourceHttpMessageConverter				: XML 데이터를 처리
	 * 5. AllEncompassingFormHttpMessageConverter	: HTML form 데이터를 처리
	 * 6. Jaxb2RootElementHttpMessageConverter		: Java Architecture for XML Binding (JAXB)와 함께 XML 데이터 처리 ([xml 데이터 <--> java 객체] 변환에 특화)
	 * 7. MappingJackson2HttpMessageConverter		: JSON 데이터를 처리 [json 데이터 <--> java 객체] 변환에 특화)
	 */
}
