package com.example.projectauth.error.exception;

/* 공통 비즈니스 에러를 위한 익셉션 */
public class BusinessException extends RuntimeException { // Custom 예외를 처리할 Exception 클래스. RuntimeException 을 상속받아  Unchecked Exception 으로 활용한다.

    private final ErrorCode errorCode; // 생성자로 ErrorCode 를 받는다.


    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}