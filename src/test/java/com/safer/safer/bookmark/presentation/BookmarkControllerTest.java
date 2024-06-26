package com.safer.safer.bookmark.presentation;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.safer.safer.auth.dto.UserInfo;
import com.safer.safer.auth.presentation.AuthArgumentResolver;
import com.safer.safer.bookmark.application.BookmarkService;
import com.safer.safer.bookmark.domain.ResourceType;
import com.safer.safer.common.ControllerTest;
import com.safer.safer.facility.application.FacilityService;
import com.safer.safer.facility.domain.FacilityType;
import com.safer.safer.facility.dto.report.FacilityReportRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@WebMvcTest(BookmarkController.class)
public class BookmarkControllerTest extends ControllerTest {

    private static final String DEFAULT_URL = "/api/bookmarks";

    @MockBean
    BookmarkService bookmarkService;
    @MockBean
    AuthArgumentResolver authArgumentResolver;

    @Test
    @DisplayName("장소 즐겨찾기 등록")
    void saveBookmark() throws Exception {
        //given
        UserInfo userInfo = UserInfo.of(1L);

        //when
        doNothing().when(bookmarkService).saveBookmark(userInfo, "123", ResourceType.OpenAPI);
        when(authArgumentResolver.supportsParameter((MethodParameter) notNull()))
                .thenReturn(true);
        when(authArgumentResolver.resolveArgument(
                (MethodParameter) notNull(),
                (ModelAndViewContainer) notNull(),
                (NativeWebRequest) notNull(),
                (WebDataBinderFactory) notNull()
        )).thenReturn(userInfo);

        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post(DEFAULT_URL)
                                .param("placeId", String.valueOf(123))
                                .param("resource", "OpenAPI")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer accessToken")
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("즐겨찾기 API")
                                                .description("장소 즐겨찾기 추가")
                                                .requestHeaders(headerWithName("Authorization").description("JWT AccessToken"))
                                                .requestFields(
                                                        fieldWithPath("placeId").description("즐겨찾기에 추가할 장소 ID"),
                                                        fieldWithPath("resource").description("장소 출처 구분 (OpenAPI / TMap)")
                                                )
                                                .requestSchema(Schema.schema("BookmarkSchema"))
                                                .build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("장소 즐겨찾기 해제")
    void deleteBookmark() throws Exception {
        //given
        UserInfo userInfo = UserInfo.of(1L);

        //when
        doNothing().when(bookmarkService).deleteBookmark(userInfo, "123", ResourceType.OpenAPI);
        when(authArgumentResolver.supportsParameter((MethodParameter) notNull()))
                .thenReturn(true);
        when(authArgumentResolver.resolveArgument(
                (MethodParameter) notNull(),
                (ModelAndViewContainer) notNull(),
                (NativeWebRequest) notNull(),
                (WebDataBinderFactory) notNull()
        )).thenReturn(userInfo);

        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.delete(DEFAULT_URL)
                                .param("placeId", String.valueOf(123))
                                .param("resource", "OpenAPI")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer accessToken")
                )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("즐겨찾기 API")
                                                .description("장소 즐겨찾기 해제")
                                                .requestHeaders(headerWithName("Authorization").description("JWT AccessToken"))
                                                .requestFields(
                                                        fieldWithPath("placeId").description("즐겨찾기에서 해제할 장소 ID"),
                                                        fieldWithPath("resource").description("장소 출처 구분 (OpenAPI / TMap)")
                                                )
                                                .build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("사용자의 즐겨찾기 장소 목록 조회")
    void findBookmarks() throws Exception {
        //given
        UserInfo userInfo = UserInfo.of(1L);

        //when
        doNothing().when(bookmarkService).findBookmarks(userInfo);
        when(authArgumentResolver.supportsParameter((MethodParameter) notNull()))
                .thenReturn(true);
        when(authArgumentResolver.resolveArgument(
                (MethodParameter) notNull(),
                (ModelAndViewContainer) notNull(),
                (NativeWebRequest) notNull(),
                (WebDataBinderFactory) notNull()
        )).thenReturn(userInfo);

        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(DEFAULT_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer accessToken")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(
                        MockMvcRestDocumentationWrapper.document(
                                "{class-name}/{method-name}",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(
                                        ResourceSnippetParameters.builder()
                                                .tag("즐겨찾기 API")
                                                .description("즐겨찾기 목록 조회")
                                                .requestHeaders(headerWithName("Authorization").description("JWT AccessToken"))
                                                .responseFields(
                                                        fieldWithPath("placeId").description("즐겨찾기 장소 ID"),
                                                        fieldWithPath("resource").description("장소 출처 구분 (OpenAPI / TMap)")
                                                )
                                                .build()
                                )
                        )
                );
    }
}
