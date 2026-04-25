package com.practice.cursor.domain.member.dto.request;

public record MemberCreateServiceRequest(
        String loginId,
        String password,
        String nickname
) {

    public static MemberCreateServiceRequest of(String loginId, String password, String nickname) {
        return new MemberCreateServiceRequest(loginId, password, nickname);
    }
}
