package com.practice.cursor.domain.auth.service;

import com.practice.cursor.global.exception.CustomException;
import com.practice.cursor.global.exception.ErrorCode;
import com.practice.cursor.global.security.SecurityAuthenticationProvider;
import com.practice.cursor.global.security.SecurityUser;
import com.practice.cursor.global.service.TokenRedisService;
import com.practice.cursor.global.util.JwtUtil;
import com.practice.cursor.domain.auth.dto.request.LoginServiceRequest;
import com.practice.cursor.domain.auth.dto.response.TokenResponse;
import com.practice.cursor.domain.member.entity.Member;
import com.practice.cursor.domain.member.entity.Role;
import com.practice.cursor.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 관련 비즈니스 로직을 처리하는 서비스.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final SecurityAuthenticationProvider authenticationProvider;
    private final JwtUtil jwtUtil;
    private final TokenRedisService tokenRedisService;
    private final MemberRepository memberRepository;

    /**
     * 로그인을 처리한다.
     */
    @Transactional
    public TokenResponse login(LoginServiceRequest request) {
        // 인증 처리
        Authentication authRequest = new UsernamePasswordAuthenticationToken(
                request.loginId(), 
                request.password()
        );
        Authentication authentication = authenticationProvider.authenticate(authRequest);
        
        // 인증된 사용자 정보 추출
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Long memberId = securityUser.getMemberId();
        Role role = securityUser.getRole();
        
        // 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(memberId, role);
        String refreshToken = jwtUtil.generateRefreshToken(memberId);
        
        // Refresh Token Redis에 저장
        tokenRedisService.saveRefreshToken(memberId, refreshToken);
        
        return TokenResponse.of(accessToken, refreshToken);
    }

    /**
     * 토큰을 재발급한다. (Refresh Token Rotation 적용)
     */
    @Transactional
    public TokenResponse reissue(String refreshToken) {
        // Refresh Token 유효성 검증
        jwtUtil.validateToken(refreshToken);
        
        // memberId 추출
        Long memberId = jwtUtil.extractMemberId(refreshToken);
        
        // Redis에 저장된 Refresh Token과 일치 여부 확인
        tokenRedisService.validateRefreshToken(memberId, refreshToken);
        
        // 기존 Refresh Token 삭제
        tokenRedisService.deleteRefreshToken(memberId);
        
        // Member 조회하여 실제 Role 가져오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Role role = member.getRole();
        String newAccessToken = jwtUtil.generateAccessToken(memberId, role);
        String newRefreshToken = jwtUtil.generateRefreshToken(memberId);
        
        // 새 Refresh Token Redis에 저장
        tokenRedisService.saveRefreshToken(memberId, newRefreshToken);
        
        return TokenResponse.of(newAccessToken, newRefreshToken);
    }

    /**
     * 로그아웃을 처리한다.
     */
    @Transactional
    public void logout(String accessToken, Long memberId) {
        // Access Token 블랙리스트에 추가
        long remainingTime = jwtUtil.getTokenRemainingTime(accessToken);
        tokenRedisService.addToBlacklist(accessToken, remainingTime);
        
        // Refresh Token 삭제
        tokenRedisService.deleteRefreshToken(memberId);
    }
}
