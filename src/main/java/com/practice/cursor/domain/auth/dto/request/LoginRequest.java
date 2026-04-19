package com.practice.cursor.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 로그인 요청 DTO.
 */
public record LoginRequest(
        @NotBlank(message = "로그인 ID는 필수입니다.")
        String loginId,

        @NotBlank(message = "비밀번호는 필수입니다.")
        String password) {
}
