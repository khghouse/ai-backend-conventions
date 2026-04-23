package com.practice.cursor.domain.member.entity;

import com.practice.cursor.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 엔티티.
 *
 * <p><b>테이블 명명:</b> 엔티티 클래스명과 동일하게 {@code Member}를 테이블명으로 사용한다.
 * SQL 예약어·DB 식별자 규칙 등으로 클래스명을 그대로 테이블명으로 쓸 수 없는 경우에는
 * {@code @Table(name = "...")}를 조정해야 하며, 그때는 <strong>본 주석에 변경 사유를 반드시
 * 남긴다</strong> (리뷰·유지보수 시 식별 가능한 알림).
 */
@Entity
@Table(name = "Member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    private static final int LOGIN_ID_MIN_LENGTH = 4;
    private static final int LOGIN_ID_MAX_LENGTH = 20;
    private static final int NICKNAME_MIN_LENGTH = 2;
    private static final int NICKNAME_MAX_LENGTH = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = LOGIN_ID_MAX_LENGTH)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = NICKNAME_MAX_LENGTH)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder(access = AccessLevel.PRIVATE)
    private Member(String loginId, String password, String nickname, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    /**
     * 새 회원을 생성한다. 로그인 ID는 4~20자, 닉네임은 2~10자여야 한다.
     */
    public static Member create(String loginId, String password, String nickname) {
        validateLoginId(loginId);
        validateNickname(nickname);
        requireNonBlankPassword(password);
        
        return Member.builder()
                .loginId(loginId.trim())
                .password(password)
                .nickname(nickname.trim())
                .role(Role.USER)
                .build();
    }

    /**
     * 닉네임을 변경한다.
     */
    public void updateNickname(String nickname) {
        validateNickname(nickname);
        this.nickname = nickname.trim();
    }

    /**
     * 비밀번호를 변경한다.
     */
    public void updatePassword(String password) {
        requireNonBlankPassword(password);
        this.password = password;
    }

    /**
     * 관리자 권한을 부여한다.
     */
    public void grantAdminRole() {
        this.role = Role.ADMIN;
    }

    private static void validateLoginId(String loginId) {
        if (loginId == null || loginId.isBlank()) {
            throw new IllegalArgumentException("로그인 ID는 필수이며 비어 있을 수 없습니다.");
        }
        String trimmedLoginId = loginId.trim();
        if (trimmedLoginId.length() < LOGIN_ID_MIN_LENGTH || trimmedLoginId.length() > LOGIN_ID_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "로그인 ID는 " + LOGIN_ID_MIN_LENGTH + "자 이상 " + LOGIN_ID_MAX_LENGTH + "자 이하여야 합니다.");
        }
    }

    private static void validateNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("닉네임은 필수이며 비어 있을 수 없습니다.");
        }
        String trimmedNickname = nickname.trim();
        if (trimmedNickname.length() < NICKNAME_MIN_LENGTH || trimmedNickname.length() > NICKNAME_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "닉네임은 " + NICKNAME_MIN_LENGTH + "자 이상 " + NICKNAME_MAX_LENGTH + "자 이하여야 합니다.");
        }
    }

    private static void requireNonBlankPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수이며 비어 있을 수 없습니다.");
        }
    }
}
