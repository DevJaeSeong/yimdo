package egovframework.web.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class YimdoControllerAdvice {
	
	/**
	 * 유효성 검사중 MethodArgumentNotValidException 발생시 처리로직.
	 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleWrongArgument(MethodArgumentNotValidException ex) {

    	StringBuilder errorString = new StringBuilder();
    	
        ex.getBindingResult().getAllErrors().forEach(error -> {
        	
            //String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            
            errorString.append(errorMessage);
            errorString.append(", ");
        });
        
        errorString.setLength(errorString.length() - 2);

        ResponseEntity<String> responseEntity = ResponseEntity.badRequest().body(errorString.toString());
        
        log.debug("=> {}", responseEntity);
        return responseEntity;
    }
}
