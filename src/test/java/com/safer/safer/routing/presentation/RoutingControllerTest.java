package com.safer.safer.routing.presentation;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.safer.safer.common.ControllerTest;
import com.safer.safer.facility.domain.FacilityType;
import com.safer.safer.facility.dto.FacilityDetailResponse;
import com.safer.safer.facility.dto.FacilityDistanceResponse;
import com.safer.safer.routing.application.RoutingService;
import com.safer.safer.routing.dto.SearchResponse;
import com.safer.safer.routing.dto.tmap.PlaceDetailResponse;
import com.safer.safer.routing.dto.tmap.PlaceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
                        List.of(new PlaceResponse(
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

    @Test
    @DisplayName("TMap 장소 상세 조회")
    void findPlace() throws Exception {
        //given
        when(routingService.findPlace(anyString()))
                .thenReturn(new PlaceDetailResponse(
                        "버섯잔치집 본점",
                        "샤브샤브",
                        "서울 강동구 성내로 52",
                        "02-470-0092",
                       false,
                                "정보 없음",
                                """
                                KBS VJ특공대, 2TV생생정보, 무한지대큐, MBC DRAMA 
                                식신원정대, 찾아라맛있는TV, 생방송오늘저녁, SBS 웰빙맛사냥, 
                                생방송투데이, 잘먹고잘사는법, 생활경제, comedyTV 맛있는녀석들에
                                 방송된 버섯잔치집은 직접 키운 친환경무공해 버섯을 무한리필 
                                 해주는 버섯요리전문점입니다. 대표적인 버섯샤브샤브는 노루궁뎅이 버섯을
                                  포함한 무공해 버섯을 제공하며, 얼큰한 맛과 담백한 맛의 육수
                                   두가지를 동시에 맛보실 수 있습니다. 또한 잔치스페셜, 큰버섯샤브전골, 통버섯불낙전골,
                                    통자연송이전골 등의 메뉴도 준비되어 있습니다.
                                """,
                       "[영업시간]11:00~22:00",
                       "11:00~22:00"
                ));
        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(DEFAULT_URL+"/places/{placeId}", "1128622")
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
                                                .description("장소 상세 조회")
                                                .pathParameters(
                                                        parameterWithName("placeId").description("TMap 장소 id")
                                                )
                                                .responseFields(
                                                        fieldWithPath("name").description("장소 이름"),
                                                        fieldWithPath("category").description("장소 카테고리"),
                                                        fieldWithPath("address").description("장소 도로명주소"),
                                                        fieldWithPath("phoneNumber").description("장소 전화번호"),
                                                        fieldWithPath("parkable").description("주차 가능 유무"),
                                                        fieldWithPath("routeInfo").description("찾아가는 길 설명"),
                                                        fieldWithPath("description").description("장소 소개"),
                                                        fieldWithPath("additionalInfo").description("장소 부가 정보"),
                                                        fieldWithPath("openingHours").description("이용 가능 시간")
                                                )
                                                .responseSchema(Schema.schema("PlaceDetailResponse"))
                                                .build()
                                )
                        )
                );
    }
}
