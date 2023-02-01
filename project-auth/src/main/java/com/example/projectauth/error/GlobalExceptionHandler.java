package com.example.projectauth.error;

import com.example.projectauth.error.exception.BusinessException;
import com.example.projectauth.error.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice // 컨트롤러 전역에서 발생할 수 있는 예외를 잡아 Throw 한다.
@Slf4j // 해당 어노테이션이 선언된 클래스에 자동으로 로그 객체를 생성한다. -> 로깅 관련 메서드 사용할 수 있다.
public class GlobalExceptionHandler { //전역 예외 핸들링용 클래스 생성
    // ResponseEntity<T>는 HTTP Request 에 대한 응답 데이터를 포함하는 클래스로, <Type>에 해당하는 데이터와 HTTP 상태 코드를 함께 리턴할 수 있다.
    // ErrorResponse 형식으로 예외 정보를 Response 로 내려준다.

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생 시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할 경우 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class) // 특정 클래스에서 발생할 수 있는 예외를 잡아 Throw 한다.(특정 Exception 별도 처리)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e); // 요청 데이터가 넘어와 validation annotation 을 통해 검증을 하던 중, 검증에 실패하면 생기는 예외
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_USERNAME, e.getBindingResult()); //
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /*
      @ModelAttribute 으로 binding error 발생 시 BindException 발생한다.
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_USERNAME, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생한다.
     * 주로 @RequestParam enum 으로 binding 못 했을 경우 발생
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        final ErrorResponse response = ErrorResponse.of(e);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 지원하지 않은 HTTP method 를 호출할 경우에 발생한다.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우에 발생한다.
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.HANDLE_ACCESS_DENIED);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.HANDLE_ACCESS_DENIED.getStatus()));
    }

    /**
     * 비즈니스 요구사항에 따른 Exception
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        log.error("handleEntityNotFoundException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    /**
     * 그 밖에 발생하는 모든 예외 처리, Null Point Exception 등등
     * 개발자가 직접 핸들링해서 다른 예외로 Throw 하지 않으면 모두 이곳으로 모인다.
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleEntityNotFoundException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
