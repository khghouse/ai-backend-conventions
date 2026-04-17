package com.practice.cursor.support;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Service + Repository 실제 연동 테스트 및 E2E 통합 테스트용 추상 클래스.
 * 
 * <p>실제 Spring Context를 띄워 Service와 Repository 간 데이터 연동을 검증한다.
 * Mock으로는 검증할 수 없는 JPA 영속성 컨텍스트, 트랜잭션 전파, 지연 로딩 동작을 포함한다.
 * 
 * <p><strong>주의:</strong> {@code @Transactional}은 부모 클래스에 선언하지 않는다.
 * 트랜잭션 경계가 중요한 테스트에서 실제 동작과 달라지는 문제를 방지하기 위함이다.
 * 데이터 격리를 위해 이 클래스를 상속받는 각 테스트 클래스에 반드시 {@code @Transactional}을 선언한다.
 * 
 * @see org.springframework.transaction.annotation.Transactional
 */
@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationTestSupport {
}
