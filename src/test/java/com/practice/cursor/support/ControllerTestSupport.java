package com.practice.cursor.support;

import com.practice.cursor.domain.todo.service.TodoService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

/**
 * Spring Security 포함 Controller 슬라이스 테스트용 추상 클래스.
 * 
 * <p>{@code @WebMvcTest}로 Controller 레이어만 로딩하여 테스트한다.
 * 모든 MockBean은 부모 클래스에서 관리하여 자식 테스트 클래스의 설정을 최소화한다.
 */
@WebMvcTest
@ActiveProfiles("test")
public abstract class ControllerTestSupport {

    @MockBean
    protected TodoService todoService;
}
