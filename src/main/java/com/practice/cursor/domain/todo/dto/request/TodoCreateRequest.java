package com.practice.cursor.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoCreateRequest(
        @NotBlank(message = "제목은 필수입니다.")
        @Size(min = 2, max = 50, message = "제목은 2자 이상 50자 이하여야 합니다.")
        String title,

        @Size(max = 500, message = "내용은 최대 500자까지 입력할 수 있습니다.")
        String content) {}
