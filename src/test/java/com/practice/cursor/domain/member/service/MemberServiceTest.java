package com.practice.cursor.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.practice.cursor.domain.member.dto.request.MemberCreateServiceRequest;
import com.practice.cursor.domain.member.entity.Member;
import com.practice.cursor.domain.member.entity.Role;
import com.practice.cursor.domain.member.repository.MemberRepository;
import com.practice.cursor.global.exception.CustomException;
import com.practice.cursor.global.exception.ErrorCode;
import com.practice.cursor.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class MemberServiceTest extends IntegrationTestSupport {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("유효한 회원 생성 요청이면 암호화된 비밀번호로 회원을 저장한다")
    void register_validRequest_persistsMemberWithEncodedPassword() {
        // given
        MemberCreateServiceRequest request = MemberCreateServiceRequest.of("tester", "password", "테스터");

        // when
        Member member = memberService.register(request);

        // then
        assertThat(member.getId()).isNotNull();
        assertThat(member.getLoginId()).isEqualTo("tester");
        assertThat(member.getNickname()).isEqualTo("테스터");
        assertThat(member.getRole()).isEqualTo(Role.USER);
        assertThat(member.getPassword()).isNotEqualTo("password");
        assertThat(passwordEncoder.matches("password", member.getPassword())).isTrue();
    }

    @Test
    @DisplayName("중복된 로그인 ID로 회원 생성 요청을 하면 실패한다")
    void register_duplicateLoginId_throwsCustomException() {
        // given
        createMember("tester", "password", "기존회원");
        MemberCreateServiceRequest request = MemberCreateServiceRequest.of("tester", "password", "새회원");

        // when & then
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DUPLICATE_LOGIN_ID.getMessage());
    }

    @Test
    @DisplayName("중복된 닉네임으로 회원 생성 요청을 하면 실패한다")
    void register_duplicateNickname_throwsCustomException() {
        // given
        createMember("tester", "password", "테스터");
        MemberCreateServiceRequest request = MemberCreateServiceRequest.of("newuser", "password", "테스터");

        // when & then
        assertThatThrownBy(() -> memberService.register(request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DUPLICATE_NICKNAME.getMessage());
    }

    @Test
    @DisplayName("존재하는 회원 ID로 조회하면 회원을 반환한다")
    void getById_existingMember_returnsMember() {
        // given
        Member savedMember = createMember("tester", "password", "테스터");

        // when
        Member member = memberService.getById(savedMember.getId());

        // then
        assertThat(member.getId()).isEqualTo(savedMember.getId());
        assertThat(member.getLoginId()).isEqualTo("tester");
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID로 조회하면 실패한다")
    void getById_missingMember_throwsCustomException() {
        assertThatThrownBy(() -> memberService.getById(999L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.MEMBER_NOT_FOUND.getMessage());
    }

    private Member createMember(String loginId, String rawPassword, String nickname) {
        Member member = Member.create(loginId, passwordEncoder.encode(rawPassword), nickname);
        return memberRepository.save(member);
    }
}
