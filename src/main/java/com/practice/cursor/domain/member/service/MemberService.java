package com.practice.cursor.domain.member.service;

import com.practice.cursor.domain.member.dto.request.MemberCreateServiceRequest;
import com.practice.cursor.domain.member.entity.Member;
import com.practice.cursor.domain.member.repository.MemberRepository;
import com.practice.cursor.global.exception.CustomException;
import com.practice.cursor.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member register(MemberCreateServiceRequest request) {
        validateDuplicateMember(request);

        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = Member.create(request.loginId(), encodedPassword, request.nickname());
        return memberRepository.save(member);
    }

    public Member getById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private void validateDuplicateMember(MemberCreateServiceRequest request) {
        if (memberRepository.existsByLoginId(request.loginId().trim())) {
            throw new CustomException(ErrorCode.DUPLICATE_LOGIN_ID);
        }

        if (memberRepository.existsByNickname(request.nickname().trim())) {
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }
}
