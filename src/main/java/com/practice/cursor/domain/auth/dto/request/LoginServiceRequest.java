package com.practice.cursor.domain.auth.dto.request;

/**
 * 로그인 서비스 요청 DTO.
 */
public record LoginServiceRequest(
        String loginId,
        String password) {

    /**
     * LoginRequest로부터 LoginServiceRequest를 생성한다.
     */
    public static LoginServiceRequest from(LoginRequest request) {
        return new LoginServiceRequest(request.loginId(), request.password());
    }
}
