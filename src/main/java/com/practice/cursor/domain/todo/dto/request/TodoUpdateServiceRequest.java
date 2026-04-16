package com.practice.cursor.domain.todo.dto.request;

public record TodoUpdateServiceRequest(
        String title,
        String content) {

    public static TodoUpdateServiceRequest from(TodoUpdateRequest request) {
        return new TodoUpdateServiceRequest(request.title(), request.content());
    }

    public static TodoUpdateServiceRequest of(String title, String content) {
        return new TodoUpdateServiceRequest(title, content);
    }
}
