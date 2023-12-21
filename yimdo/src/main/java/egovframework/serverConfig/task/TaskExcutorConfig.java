package egovframework.serverConfig.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import egovframework.serverConfig.ServerConfig;

/**
 * TaskExecutor 설정 클래스.
 */
@Configuration
@EnableAsync
public class TaskExcutorConfig {

	@Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
    	
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        
        // 최대 작업 가능한 수 = 코어 스레드 수 + 대기 큐 크기
        
        taskExecutor.setCorePoolSize(ServerConfig.CPU_CORE_SIZE); 	 // 기본 스레드 수 (임도 서버로 사용중인 192.168.0.125 PC는 cpu 코어수가 2임)
        taskExecutor.setMaxPoolSize(ServerConfig.CPU_CORE_SIZE * 20); // 최대 스레드 수
        taskExecutor.setQueueCapacity(50); 							 // 대기 큐 크기 (일반적으로 10 ~ 100 최대치는 Integer.MAX_VALUE 값을 설정)
        //taskExecutor.setKeepAliveSeconds(60);						 // 놀고 있는 스레드가 제거되기까지 대기해야 하는 시간(초)
        //taskExecutor.setWaitForTasksToCompleteOnShutdown(true);	 // 스레드 풀을 종료할 때 대기 중인 모든 작업이 완료될 때까지 기다릴지 여부
        //taskExecutor.setAwaitTerminationSeconds(60);				 // 스레드 풀 종료 후 완료되기를 기다리는 시간(초)
        // taskExecutor.setThreadNamePrefix("MyThreadPool-");		 // 스레드 이름의 접두사를 설정
        
        /*
         * 스레드 풀이 작업을 처리할 수 없을 때 (예: 스레드 풀이 이미 가득 차거나 스레드가 더 이상 생성할 수 없을 때)
         * 어떻게 거부된 작업을 처리할지를 설정하는 메서드.
         * 
         * 1. AbortPolicy (기본값): 스레드 풀이 가득 찼거나 스레드 생성이 불가능할 때 거부된 작업은 예외를 발생시킵니다. 
         * 	  이 예외는 대부분의 경우에 기본값으로 설정되어 있으며, 거부된 작업을 즉시 중단시킵니다.
         * 
         * 2. CallerRunsPolicy: 거부된 작업은 작업을 제출한 호출자 스레드에서 실행됩니다. 
         * 	  이 방식은 스레드 풀의 부하를 감소시키고 호출자에게 직접 처리할 기회를 제공합니다.
         * 
         * 3. DiscardPolicy: 거부된 작업을 무시하고 아무 작업도 수행하지 않습니다.
         * 	  이는 작업이 손실될 수 있지만, 스레드 풀에 작업을 추가하는 데 더 많은 리소스를 사용하지 않으며 작업을 중단시키지 않습니다.
         * 
         * 4. DiscardOldestPolicy: 거부된 작업은 스레드 풀의 대기 큐에서 가장 오래된 작업을 삭제하고 해당 작업을 수행합니다.
         * 	  이 방식은 새로운 작업을 받아들이고 가장 오래된 작업을 제거하는 방식입니다.
         */
        //taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        
        
        return taskExecutor;
    }
}
