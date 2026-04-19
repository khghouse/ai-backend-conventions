package com.practice.cursor.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

/**
 * Spring REST Docs API 문서화 테스트용 추상 클래스.
 *
 * <p>{@code @WebMvcTest} 없이 {@code @ExtendWith(RestDocumentationExtension.class)}만 사용한다.
 * Spring Context를 띄우지 않으므로 {@code ControllerTestSupport}보다 가볍고 빠르게 동작한다.
 *
 * <p>{@code MockMvcBuilders.standaloneSetup(initController())}을 사용하여 테스트 대상 Controller만 등록한다.
 * {@code initController()}를 추상 메서드로 강제하여 각 테스트 클래스가 자신의 Controller만 등록하도록 한다.
 *
 * <p>{@code document} 필드를 {@code alwaysDo()}에 등록하여 모든 테스트에 자동으로 문서화가 적용되도록 한다.
 * 스니펫 경로는 {@code {class-name}/{method-name}} 패턴을 사용한다.
 *
 * <p>{@code CharacterEncodingFilter("UTF-8", true)}를 추가하여 한글 응답이 깨지지 않도록 한다.
 * {@code ObjectMapper}는 필드 직접 초기화로 생성하고 {@code JavaTimeModule}을 등록하여 {@code LocalDateTime} 직렬화를 보장한다.
 *
 * <p>{@code ControllerTestSupport}는 Spring Security 통합이 필요한 Controller 테스트에,
 * {@code RestDocsSupport}는 API 문서화 목적의 테스트에 사용한다. 용도에 맞게 선택하여 상속한다.
 */
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsSupport {

    protected MockMvc mockMvc;
    protected RestDocumentationResultHandler document;
    protected ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        this.document = document("{class-name}/{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));

        // Jackson 설정이 적용된 ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());

        this.mockMvc = MockMvcBuilders.standaloneSetup(initController())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(provider))
                .alwaysDo(document)
                .build();
    }

    /**
     * 테스트 대상 Controller를 반환하는 추상 메서드.
     * 각 테스트 클래스에서 자신의 Controller 인스턴스를 반환하도록 구현한다.
     *
     * @return 테스트 대상 Controller 인스턴스
     */
    protected abstract Object initController();
}
