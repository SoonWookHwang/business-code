package com.example.projectauth.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode { // 효율적으로 예외를 관리하기 위해 모든 예외를 하나의 Enum 클래스로 관리한다.

    /*
    400 BAD_REQUEST : 잘못된 요청
    401 UNAUTHORIZED : 클라이언트가 인증되지 않았거나, 유효한 인증 정보가 부족하여 요청이 거부됨
    403 FORBIDDEN : 클라이언트가 해당 요청에 대한 권한이 없는 경우(로그인 했지만 접근 권한 없는 무언가 요청할 때)
    404 NOT_FOUND : Resource 를 찾을 수 없음
    405 METHOD_NOT_ALLOWED : 요청 줄에 지정된 메서드가 요청 URI 로 식별되는 리소스에 허용되지 않는 상황
    500 INTERNAL_SERVER_ERROR : 예외적인 또는 예측하지 못한 서버 에러. 서버 에러 총칭
     */

    // Common
    INVALID_INPUT_VALUE(400, " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "Server Error"),
    INVALID_TYPE_VALUE(400, " Invalid Type Value"),

    // 유저
    HANDLE_ACCESS_DENIED(401, "로그인이 필요합니다."),
    INVALID_INPUT_USERNAME(400, "아이디를 4자 이상 입력해 주세요"),
    INVALID_INPUT_NICKNAME(400, "닉네임을 2자 이상 입력해 주세요"),
    NOTEQUAL_INPUT_PASSWORD(400, "비밀번호가 일치하지 않습니다"),
    INVALID_PASSWORD(400, "비밀번호를 4자 이상 입력해 주세요"),
    INVALID_USERNAME(400, "알파벳 대소문자와 숫자로만 입력해 주세요"),
    INVALID_INPUT_INTRO(400, "자기소개를 입력해 주세요."),
    NOT_AUTHORIZED(403, "작성자만 수정 및 삭제를 할 수 있습니다."),
    USERNAME_DUPLICATION(400, "이미 등록된 아이디입니다."),
    NICKNAME_DUPLICATION(400,"이미 등록된 닉네입입니다."),
    LOGIN_INPUT_INVALID(400, "로그인 정보를 다시 확인해 주세요."),
    NOTFOUND_USER(404, "해당 이름의 사용자가 존재하지 않습니다."),

    // 게시글
    INVALID_INPUT_TITLE(400, "제목을 입력해 주세요"),
    INVALID_INPUT_CONTENTS(400, "내용을 입력해 주세요"),
    INVALID_INPUT_FILEPATH(400, "파일을 업로드해 주세요"),
    NOTFOUND_DATA(404, "해당 자료 존재하지 않습니다."),
    UPLOAD_FAILED(400, "파일 업로드에 실패했습니다."),
    INVALID_FILE_EXTENSION(400, "파일 확장자를 확인해 주세요."),

    // 댓글
    NOTFOUND_COMMENT(404, "해당 댓글이 존재하지 않습니다."),

    ;
    private final int status; // 상태 코드를 상수로 선언해둔 HttpStatus 타입의 멤버, 예외에 대한 상태 코드(status)와 이름(error)을 처리하는 데 사용된다.
    private final String message; // 예외에 대한 응답 메시지(message)를 처리하는 데 사용되는 멤버

    ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public int getStatus() {
        return status;
    }
}
