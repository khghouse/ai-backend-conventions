package com.practice.cursor.domain.todo.dto.response;

import com.practice.cursor.domain.todo.entity.Todo;
import java.time.LocalDateTime;

public record TodoResponse(
        Long id,
        String title,
        String content,
        boolean completed,
        LocalDateTime createDateTime,
        LocalDateTime modifiedDateTime) {

    /**
     * Todo 엔티티로부터 TodoResponse를 생성한다.
     */
    public static TodoResponse from(Todo todo) {
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContent(),
                todo.isCompleted(),
                todo.getCreateDateTime(),
                todo.getModifiedDateTime());
    }
}
