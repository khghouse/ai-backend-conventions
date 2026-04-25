package com.practice.cursor.domain.auth.dto.request;

public record RegisterServiceRequest(
        String loginId,
        String password,
        String nickname
) {

    public static RegisterServiceRequest from(RegisterRequest request) {
        return new RegisterServiceRequest(
                request.loginId(),
                request.password(),
                request.nickname()
        );
    }
}
