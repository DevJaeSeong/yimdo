package egovframework.serverConfig.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import egovframework.serverConfig.ServerConfig;

/**
 * 스케줄러 설정 클래스.
 */
@Configuration
@EnableScheduling
public class TaskSchedulerConfig {
	
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
    	
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        
        scheduler.setPoolSize(ServerConfig.CPU_CORE_SIZE);			// 스케줄링을 수행하는 스레드 풀의 크기
        //scheduler.setThreadPriority(5);							// 스레드의 우선 순위
        //scheduler.setDaemon(true);								// 스레드를 백그라운드(daemon) 스레드로 설정할지 여부
        //scheduler.setAwaitTerminationSeconds(60);					// 스레드 풀이 종료될 때 대기하는 최대 시간(초)
        //scheduler.setRemoveOnCancelPolicy(true);					// 스케줄링 작업이 취소될 때 해당 작업을 스케줄링 큐에서 제거할지 여부
        //scheduler.setThreadNamePrefix(""); 						// 스레드 이름의 접두사를 설정
        //scheduler.setErrorHandler(new SchedulerErrorHandler());	// 스케줄링 실행중 오류 발생시 처리 로직
        
        /*
         * AbortPolicy (기본값): 거부된 작업은 예외를 발생시킵니다.
         * 						 이 예외는 거부된 작업을 즉시 중단시킵니다.
         * 
         * CallerRunsPolicy: 거부된 작업은 작업을 제출한 호출자 스레드에서 실행됩니다.
         * 					 이 방식은 스레드 풀의 부하를 감소시키고 호출자에게 직접 처리할 기회를 제공합니다.
         * 
         * DiscardPolicy: 거부된 작업은 무시하고 아무 작업도 수행하지 않습니다.
         * 				  이는 작업이 손실될 수 있지만, 스레드 풀에 작업을 추가하는 데 더 많은 리소스를 사용하지 않으며 작업을 중단시키지 않습니다.
         *
         * DiscardOldestPolicy: 거부된 작업은 스레드 풀의 대기 큐에서 가장 오래된 작업을 삭제하고 해당 작업을 수행합니다.
         * 						이 방식은 새로운 작업을 받아들이고 가장 오래된 작업을 제거하는 방식입니다.
		 */
        //scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        
        return scheduler;
    }
    
    /**
     * 스케쥴러 에러 발생시 처리 로직을 가진 핸들러.
     */
    /*
	private class SchedulerErrorHandler implements ErrorHandler {

		@Override
		public void handleError(Throwable t) {
			
		}
    }
    */
}
