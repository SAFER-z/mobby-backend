package com.safer.safer.routing.presentation;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.safer.safer.common.ControllerTest;
import com.safer.safer.facility.domain.FacilityType;
import com.safer.safer.facility.dto.CoordinateRequest;
import com.safer.safer.facility.dto.FacilityDistanceResponse;
import com.safer.safer.routing.application.RoutingService;
import com.safer.safer.routing.dto.SearchResponse;
import com.safer.safer.routing.dto.address.AddressResponse;
import com.safer.safer.station.dto.StationDistanceResponse;
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
        CoordinateRequest userCoordinate = CoordinateRequest.of(coordinate.latitude(), coordinate.longitude());
        when(routingService.searchByKeyword("query", userCoordinate))
                .thenReturn(SearchResponse.of(
                        List.of(new FacilityDistanceResponse() {
                            public Long getId() {return 1L;}
                            public String getName() {return "강변 공영주차장";}
                            public String getAddress() {return "서울특별시 성동구 둘레길 47-5 (성수동1가)";}
                            public FacilityType getCategory() {return FacilityType.PARKING_LOT;}
                            public double getDistance() {return 100;}
                        }),
                        List.of(new StationDistanceResponse() {
                            public Long getId() {return 1L;}
                            public String getName() {return "역삼역";}
                            public String getAddress() {return "서울특별시 성동구 둘레길 47-5 (성수동1가)";}
                            public String getLine() {return "1호선";}
                            public double getDistance() {return 100;}
                        }),
                        List.of(AddressResponse.of("서울특별시 성동구 둘레길 47-5 (성수동1가)"))
                ));
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
                                                .description("편의시설, 역, 주소 통합 검색")
                                                .queryParameters(
                                                        parameterWithName("lat").description("사용자 현재 위도"),
                                                        parameterWithName("lon").description("사용자 현재 경도"),
                                                        parameterWithName("q").description("검색 키워드")
                                                )
                                                .responseFields(
                                                        fieldWithPath("stations").description("역 검색 결과 목록"),
                                                        fieldWithPath("stations[].id").description("역 id"),
                                                        fieldWithPath("stations[].name").description("역 이름"),
                                                        fieldWithPath("stations[].address").description("역 주소"),
                                                        fieldWithPath("stations[].line").description("역 노선명"),
                                                        fieldWithPath("stations[].distance").description("사용자 위치 기준 역 거리"),

                                                        fieldWithPath("facilities").description("편의시설 검색 결과 목록"),
                                                        fieldWithPath("facilities[].id").description("편의시설 id"),
                                                        fieldWithPath("facilities[].name").description("편의시설 이름"),
                                                        fieldWithPath("facilities[].address").description("편의시설 주소"),
                                                        fieldWithPath("facilities[].category").description("편의시설 종류"),
                                                        fieldWithPath("facilities[].distance").description("사용자 위치 기준 편의시설 거리"),

                                                        fieldWithPath("addresses").description("도로명주소 검색 결과 목록"),
                                                        fieldWithPath("addresses[].address").description("도로명주소")
                                                )
                                                .responseSchema(Schema.schema("SearchResponse"))
                                                .build()
                                )
                        )
                );
    }
}
