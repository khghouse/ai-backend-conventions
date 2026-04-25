package com.practice.cursor.domain.auth.dto.response;

import com.practice.cursor.domain.member.entity.Member;
import com.practice.cursor.domain.member.entity.Role;

public record RegisterResponse(
        Long id,
        String loginId,
        String nickname,
        Role role
) {

    public static RegisterResponse from(Member member) {
        return new RegisterResponse(
                member.getId(),
                member.getLoginId(),
                member.getNickname(),
                member.getRole()
        );
    }
}
