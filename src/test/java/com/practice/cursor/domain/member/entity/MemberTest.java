package com.practice.cursor.domain.member.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.practice.cursor.global.exception.CustomException;
import com.practice.cursor.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("로그인 ID·비밀번호·닉네임이 유효하면 회원을 생성할 수 있다")
    void create_validValues_returnsMember() {
        Member member = Member.create("tester", "encoded-password", "테스터");

        assertThat(member.getLoginId()).isEqualTo("tester");
        assertThat(member.getPassword()).isEqualTo("encoded-password");
        assertThat(member.getNickname()).isEqualTo("테스터");
        assertThat(member.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("로그인 ID가 중간 공백 없이 유효하면 trim 후 회원을 생성한다")
    void create_loginIdAndNicknameWithOuterSpaces_returnsTrimmedMember() {
        Member member = Member.create("  tester  ", "encoded-password", "  테스터  ");

        assertThat(member.getLoginId()).isEqualTo("tester");
        assertThat(member.getNickname()).isEqualTo("테스터");
    }

    @Test
    @DisplayName("로그인 ID가 비어 있으면 회원 생성에 실패한다")
    void create_blankLoginId_throwsCustomException() {
        assertThatThrownBy(() -> Member.create(" ", "encoded-password", "테스터"))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_LOGIN_ID_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("닉네임이 비어 있으면 회원 생성에 실패한다")
    void create_blankNickname_throwsCustomException() {
        assertThatThrownBy(() -> Member.create("tester", "encoded-password", " "))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_NICKNAME_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("비밀번호가 비어 있으면 회원 생성에 실패한다")
    void create_blankPassword_throwsCustomException() {
        assertThatThrownBy(() -> Member.create("tester", " ", "테스터"))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_PASSWORD_REQUIRED.getMessage());
    }
}
