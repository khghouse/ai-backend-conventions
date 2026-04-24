package com.practice.cursor.global.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.practice.cursor.global.exception.CustomException;
import com.practice.cursor.global.exception.ErrorCode;
import com.practice.cursor.domain.member.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JwtTokenProvider 단위 테스트.
 */
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        String secret = "test-secret-key-for-testing-must-be-at-least-256-bits-long-for-HS256-algorithm";
        long accessTokenExpiration = 1800000L;
        long refreshTokenExpiration = 604800000L;

        jwtTokenProvider = new JwtTokenProvider(secret, accessTokenExpiration, refreshTokenExpiration);
    }

    @Test
    @DisplayName("Access Token 생성 및 검증이 정상 동작한다")
    void generateAccessToken_validMemberIdAndRole_returnsValidToken() {
        // given
        Long memberId = 1L;
        Role role = Role.USER;

        // when
        String accessToken = jwtTokenProvider.generateAccessToken(memberId, role);

        // then
        assertThat(jwtTokenProvider.validateToken(accessToken)).isTrue();
        assertThat(jwtTokenProvider.extractMemberId(accessToken)).isEqualTo(memberId);
        assertThat(jwtTokenProvider.extractRole(accessToken)).isEqualTo(role);
        assertThat(jwtTokenProvider.extractTokenType(accessToken)).isEqualTo(JwtTokenProvider.TokenType.ACCESS);
    }

    @Test
    @DisplayName("Refresh Token 생성 및 검증이 정상 동작한다")
    void generateRefreshToken_validMemberId_returnsValidToken() {
        // given
        Long memberId = 1L;

        // when
        String refreshToken = jwtTokenProvider.generateRefreshToken(memberId);

        // then
        assertThat(jwtTokenProvider.validateToken(refreshToken)).isTrue();
        assertThat(jwtTokenProvider.extractMemberId(refreshToken)).isEqualTo(memberId);
        assertThat(jwtTokenProvider.extractTokenType(refreshToken)).isEqualTo(JwtTokenProvider.TokenType.REFRESH);
    }

    @Test
    @DisplayName("Access Token을 Refresh Token으로 검증하면 예외가 발생한다")
    void validateRefreshTokenType_accessToken_throwsCustomException() {
        // given
        String accessToken = jwtTokenProvider.generateAccessToken(1L, Role.USER);

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateRefreshTokenType(accessToken))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.TOKEN_TYPE_INVALID.getMessage());
    }

    @Test
    @DisplayName("Refresh Token을 Access Token으로 검증하면 예외가 발생한다")
    void validateAccessTokenType_refreshToken_throwsCustomException() {
        // given
        String refreshToken = jwtTokenProvider.generateRefreshToken(1L);

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateAccessTokenType(refreshToken))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.TOKEN_TYPE_INVALID.getMessage());
    }

}
