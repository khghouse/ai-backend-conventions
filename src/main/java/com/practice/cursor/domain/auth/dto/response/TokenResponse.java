package com.practice.cursor.domain.auth.dto.response;

/**
 * 토큰 응답 DTO.
 */
public record TokenResponse(
        String accessToken,
        String refreshToken) {

    /**
     * Access Token과 Refresh Token으로 TokenResponse를 생성한다.
     */
    public static TokenResponse of(String accessToken, String refreshToken) {
        return new TokenResponse(accessToken, refreshToken);
    }
}
