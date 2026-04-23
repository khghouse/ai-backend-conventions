package com.practice.cursor.domain.todo.entity;

import com.practice.cursor.global.entity.BaseEntity;
import com.practice.cursor.global.exception.CustomException;
import com.practice.cursor.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 할 일 엔티티.
 *
 * <p><b>테이블 명명:</b> 엔티티 클래스명과 동일하게 {@code Todo}를 테이블명으로 사용한다.
 * SQL 예약어·DB 식별자 규칙 등으로 클래스명을 그대로 테이블명으로 쓸 수 없는 경우에는
 * {@code @Table(name = "...")}를 조정해야 하며, 그때는 <strong>본 주석에 변경 사유를 반드시
 * 남긴다</strong> (리뷰·유지보수 시 식별 가능한 알림).
 */
@Entity
@Table(name = "Todo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends BaseEntity {

    private static final int TITLE_MIN_LENGTH = 2;
    private static final int TITLE_MAX_LENGTH = 50;
    private static final int CONTENT_MAX_LENGTH = 500;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = TITLE_MAX_LENGTH)
    private String title;

    @Column(length = CONTENT_MAX_LENGTH)
    private String content;

    @Column(nullable = false)
    private boolean deleted;

    @Column(nullable = false)
    private boolean completed;

    @Builder(access = AccessLevel.PRIVATE)
    private Todo(String title, String content, boolean deleted, boolean completed) {
        this.title = title;
        this.content = content;
        this.deleted = deleted;
        this.completed = completed;
    }

    /**
     * 새 할 일을 만든다. 제목은 트림 후 2~50자, 내용은 생략 가능하며 최대 500자이다.
     */
    public static Todo create(String title, String content) {
        requireNonBlankTitle(title);
        String trimmedTitle = title.trim();
        validateTitleLength(trimmedTitle);
        validateContent(content);
        return Todo.builder()
                .title(trimmedTitle)
                .content(content)
                .deleted(false)
                .completed(false)
                .build();
    }

    /**
     * 제목·내용을 수정한다. 삭제된 할 일은 수정할 수 없다.
     */
    public void updateTitleAndContent(String title, String content) {
        ensureNotDeleted();
        requireNonBlankTitle(title);
        String trimmedTitle = title.trim();
        validateTitleLength(trimmedTitle);
        validateContent(content);
        this.title = trimmedTitle;
        this.content = content;
    }

    /**
     * 완료 처리한다. 삭제된 할 일은 완료할 수 없다.
     */
    public void complete() {
        ensureNotDeleted();
        this.completed = true;
    }

    /**
     * 소프트 삭제한다. 이미 삭제된 경우 아무 동작도 하지 않는다.
     */
    public void delete() {
        if (deleted) {
            return;
        }
        this.deleted = true;
    }

    private void ensureNotDeleted() {
        if (deleted) {
            throw new CustomException(ErrorCode.TODO_DELETED);
        }
    }

    private static void requireNonBlankTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new CustomException(ErrorCode.TODO_TITLE_REQUIRED);
        }
    }

    private static void validateTitleLength(String trimmedTitle) {
        if (trimmedTitle.length() < TITLE_MIN_LENGTH || trimmedTitle.length() > TITLE_MAX_LENGTH) {
            throw new CustomException(ErrorCode.TODO_TITLE_LENGTH_INVALID, TITLE_MIN_LENGTH, TITLE_MAX_LENGTH);
        }
    }

    private static void validateContent(String content) {
        if (content != null && content.length() > CONTENT_MAX_LENGTH) {
            throw new CustomException(ErrorCode.TODO_CONTENT_LENGTH_EXCEEDED, CONTENT_MAX_LENGTH);
        }
    }
}
