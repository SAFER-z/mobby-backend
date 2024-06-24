package com.safer.safer.routing.presentation;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.safer.safer.common.ControllerTest;
import com.safer.safer.facility.domain.FacilityType;
import com.safer.safer.facility.dto.FacilityDistanceResponse;
import com.safer.safer.routing.application.RoutingService;
import com.safer.safer.routing.dto.SearchResponse;
import com.safer.safer.routing.dto.tmap.TMapResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@WebMvcTest(RoutingController.class)
public class RoutingControllerTest extends ControllerTest {

    private static final String DEFAULT_URL = "/api/routing";

    @MockBean
    RoutingService routingService;

    @Test
    @DisplayName("키워드로 통합 검색하기")
    void searchByKeyword() throws Exception {
        //given
        when(routingService.searchByKeyword("query", coordinate.latitude(), coordinate.longitude()))
                .thenReturn(SearchResponse.of(
                        List.of(new FacilityDistanceResponse() {
                            public Long getId() {return 1L;}
                            public String getName() {return "강변 공영주차장";}
                            public String getAddress() {return "서울특별시 성동구 둘레길 47-5 (성수동1가)";}
                            public FacilityType getCategory() {return FacilityType.PARKING_LOT;}
                            public double getDistance() {return 100;}
                        }),
                        List.of(new TMapResponse(
                                "568848",
                                "마포장애인종합복지관",
                                37.56420321,
                                126.90847192,
                                "서울특별시 마포구 성산로4길 35",
                                24.011

                        )))
                );
        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(DEFAULT_URL+"/search")
                                .param("lat", "37.5448467")
                                .param("lon", "127.0392661")
                                .param("q", "query")
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
                                                .tag("검색 API")
                                                .summary("통합 검색")
                                                .description("편의시설, TMAP 통합 검색")
                                                .queryParameters(
                                                        parameterWithName("lat").description("사용자 현재 위도"),
                                                        parameterWithName("lon").description("사용자 현재 경도"),
                                                        parameterWithName("q").description("검색 키워드")
                                                )
                                                .responseFields(
                                                        fieldWithPath("facilities").description("편의시설 검색 결과 목록"),
                                                        fieldWithPath("facilities[].id").description("편의시설 ID"),
                                                        fieldWithPath("facilities[].name").description("편의시설 이름"),
                                                        fieldWithPath("facilities[].address").description("편의시설 주소"),
                                                        fieldWithPath("facilities[].category").description("편의시설 종류"),
                                                        fieldWithPath("facilities[].distance").description("사용자 위치 기준 편의시설 거리 (km)"),

                                                        fieldWithPath("tMapResponses").description("TMAP 검색 결과 목록"),
                                                        fieldWithPath("tMapResponses[].id").description("장소 ID"),
                                                        fieldWithPath("tMapResponses[].name").description("장소 이름"),
                                                        fieldWithPath("tMapResponses[].latitude").description("장소 위도"),
                                                        fieldWithPath("tMapResponses[].longitude").description("장소 경도"),
                                                        fieldWithPath("tMapResponses[].address").description("상세 주소"),
                                                        fieldWithPath("tMapResponses[].distance").description("사용자 위치 기준 장소 거리 (km)")
                                                )
                                                .responseSchema(Schema.schema("SearchResponse"))
                                                .build()
                                )
                        )
                );
    }
}
