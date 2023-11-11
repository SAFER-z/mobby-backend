package com.safer.safer.auth.presentation;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.safer.safer.auth.application.AuthService;
import com.safer.safer.auth.dto.UserTokens;
import com.safer.safer.common.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@WebMvcTest(AuthController.class)
public class AuthControllerTest extends ControllerTest {

    private static final String DEFAULT_URL = "/api/oauth";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    @MockBean
    AuthService authService;

    @Test
    @DisplayName("소셜 아이디로 로그인하기 - 로그인 URL로 리다이렉트")
    void getLoginRedirectUri() throws Exception {
        //given
        when(authService.generateLoginRedirectUri(anyString()))
                .thenReturn("https://test.com");

        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(DEFAULT_URL+"/{provider}", "kakao")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("로그인 API")
                                                .summary("소셜 로그인 창 불러오기")
                                                .description("로그인 화면으로 리다이렉트")
                                                .pathParameters(
                                                        parameterWithName("provider").description("소셜 로그인 종류 [kakao, naver]")
                                                )
                                                .responseHeaders(
                                                        headerWithName("Location").description("리다이렉트 링크")
                                                )
                                                .build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("소셜 아이디로 로그인하기 - 인증 코드로 토큰 발급하기")
    void login() throws Exception {
        //given
        when(authService.login(anyString(), anyString()))
                .thenReturn(UserTokens.of(ACCESS_TOKEN, REFRESH_TOKEN));
        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(DEFAULT_URL+"/login/{provider}", "kakao")
                                .param("code", "testCode")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("로그인 API")
                                                .summary("토큰 발급")
                                                .description("로그인 이후 AccessToken 발급 (로그인에 성공하면 자동으로 호출됩니다.\n실제로는 로그인 화면을 요청하면 AccessToken을 바로 받을 수 있습니다.)")
                                                .pathParameters(
                                                        parameterWithName("provider").description("소셜 로그인 종류 [kakao, naver]")
                                                )
                                                .queryParameters(
                                                        parameterWithName("code").description("사용자 정보를 조회 가능한 인증 코드")
                                                )
                                                .responseFields(
                                                        fieldWithPath("accessToken").description("JWT AccessToken 값")
                                                )
                                                .responseSchema(Schema.schema("AccessTokenResponse"))
                                                .build()
                                )
                        )
                );
    }
}
