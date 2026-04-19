package com.practice.cursor.domain.member.repository;

import com.practice.cursor.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 회원 Repository.
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 로그인 ID로 회원을 조회한다.
     */
    Optional<Member> findByLoginId(String loginId);

    /**
     * 로그인 ID 존재 여부를 확인한다.
     */
    boolean existsByLoginId(String loginId);

    /**
     * 닉네임 존재 여부를 확인한다.
     */
    boolean existsByNickname(String nickname);
}
