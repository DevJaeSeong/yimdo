package egovframework.serverConfig.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.StreamUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 사용자의 요청을 복사하기 위해 {@link HttpServletRequestWrapper} 를 커스터마이징한 클래스.
 */
@Slf4j
public class YimdoServletRequestWrapper extends HttpServletRequestWrapper {

	private final Executor executor;
	private final byte[] requestData;
	private final ByteArrayInputStream inputStream;

	public YimdoServletRequestWrapper(HttpServletRequest request, Executor executor) throws IOException {

		super(request);
		
		this.requestData = StreamUtils.copyToByteArray(request.getInputStream());
		this.executor = executor;
		this.inputStream = new ByteArrayInputStream(requestData);
	}

	@Override
	public ServletInputStream getInputStream() {
		
		return new ServletInputStream() {

			@Override
			public int read() {
				
				return inputStream.read();
			}

			@Override
			public boolean isFinished() {
				
				return inputStream.available() == 0;
			}

			@Override
			public boolean isReady() {
				
				return executor != null ? true : false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				
                // 비동기 작업을 수행하는 경우, executor를 사용하여 비동기적으로 데이터를 읽어올 수 있도록 처리
                executor.execute(() -> {
                	
                    try {
                    	
                        // 데이터를 비동기적으로 읽어옴
                        while (inputStream.available() > 0) {
                        	
                            readListener.onDataAvailable();
                        }
                        
                        readListener.onAllDataRead();
                        
                    } catch (IOException e) {
                    	
                    	log.error("에러 발생");
                        readListener.onError(e);
                    }
                });
			}
		};
	}

	@Override
	public BufferedReader getReader() {
		
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}
	
	public byte[] getRequestData() {
		
		return requestData;
	}
}
