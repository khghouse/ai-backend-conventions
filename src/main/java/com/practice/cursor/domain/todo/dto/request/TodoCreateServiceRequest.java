package com.practice.cursor.domain.todo.dto.request;

public record TodoCreateServiceRequest(
        String title,
        String content) {

    public static TodoCreateServiceRequest from(TodoCreateRequest request) {
        return new TodoCreateServiceRequest(request.title(), request.content());
    }

    public static TodoCreateServiceRequest of(String title, String content) {
        return new TodoCreateServiceRequest(title, content);
    }
}
