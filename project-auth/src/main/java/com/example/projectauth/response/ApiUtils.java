package com.example.projectauth.response;

public class ApiUtils {
    public static <T> CommonResponse<T> success(int code, T result) {
        return new CommonResponse<>(code, result);
    }

    public static <T> CommonResponse<T> failed(int code, T result) {
        return new CommonResponse<>(code, result);
    }

}