package com.example.spring.test.common.response;

import com.example.spring.test.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponseEntity {

    private final int code;
    private final int status;
    private final String message;

    public ErrorResponseEntity(int code, int status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public static ErrorResponseEntity from (ErrorCode errorCode) {
        return new ErrorResponseEntity(
                errorCode.getCode(),
                errorCode.getStatus().value(),
                errorCode.getMessage());
    }

    public static ErrorResponseEntity of (ErrorCode errorCode, String detail) {
        String message = errorCode.getMessage();
        if (detail !=null && !detail.isBlank()) {
            message = message + " " + detail;
        }

        return new ErrorResponseEntity(
                errorCode.getCode(),
                errorCode.getStatus().value(),
                message);
    }
}
