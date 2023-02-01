package com.example.projectauth.error.exception;

/* 각 엔티티들을 못 찾았을 경우 예외를 던지는 Exception*/
public class EntityNotFoundException extends BusinessException{
    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
