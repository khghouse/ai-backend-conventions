package com.practice.cursor.common.response;

import com.practice.cursor.common.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorBody {

    private String code;
    private String message;

    public ErrorBody(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorBody fromErrorCode(ErrorCode errorCode) {
        return new ErrorBody(errorCode.getCode(), errorCode.getMessage());
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}

