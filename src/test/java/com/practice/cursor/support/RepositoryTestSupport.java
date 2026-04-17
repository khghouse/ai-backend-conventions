package com.practice.cursor.support;

import com.practice.cursor.common.config.JpaAuditingConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * JPA / Querydsl Repository 슬라이스 테스트용 추상 클래스.
 * 
 * <p>{@code @DataJpaTest}로 JPA 슬라이스 테스트를 작성한다.
 * {@code @Import(JpaAuditingConfig.class)}를 반드시 포함한다.
 * {@code @DataJpaTest}는 {@code @EnableJpaAuditing}이 선언된 메인 설정 클래스를 로드하지 않으므로,
 * 이를 누락하면 {@code @CreatedDate}, {@code @LastModifiedDate} 등 Auditing 필드가 채워지지 않아 테스트가 실패한다.
 * 
 * <p>{@code @DataJpaTest}는 기본적으로 {@code @Transactional}이 적용되어 있으므로 별도 선언하지 않는다.
 * 
 * <p>Querydsl을 사용하는 경우 {@code @Import(QuerydslConfig.class)}로 설정을 추가한다.
 */
@DataJpaTest
@ActiveProfiles("test")
@Import(JpaAuditingConfig.class)
public abstract class RepositoryTestSupport {
}
