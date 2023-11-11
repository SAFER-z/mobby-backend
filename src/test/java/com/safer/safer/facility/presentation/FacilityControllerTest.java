package com.safer.safer.facility.presentation;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.safer.safer.common.ControllerTest;
import com.safer.safer.facility.domain.FacilityType;
import com.safer.safer.facility.dto.FacilitiesResponse;
import com.safer.safer.facility.dto.FacilityDetailResponse;
import com.safer.safer.facility.dto.FacilityResponse;
import com.safer.safer.facility.application.FacilityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;

@WebMvcTest(FacilityController.class)
public class FacilityControllerTest extends ControllerTest {

    private static final String DEFAULT_URL = "/api/facilities";

    @MockBean
    FacilityService facilityService;

    @Test
    @DisplayName("1.5km 반경 내 편의시설 조회")
    void findFacilitiesByDistanceAndCategory() throws Exception {
        //given
        when(facilityService.findFacilitiesByDistance(coordinate, "PARKING_LOT"))
                .thenReturn(FacilitiesResponse.of(
                        List.of(new FacilityResponse(
                                1L,
                                "강변 공영주차장",
                                FacilityType.PARKING_LOT,
                                37.53696583,
                                127.0490425
                        )))
                );
        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(DEFAULT_URL)
                                .param("lat", "37.5448467")
                                .param("lon", "127.0392661")
                                .param("category", "PARKING_LOT")
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
                                                .tag("편의시설 API")
                                                .summary("편의시설 전체 조회")
                                                .description("1.5km 반경 내 편의시설 전체 조회 | 카테고리 별 조회")
                                                .queryParameters(
                                                        parameterWithName("lat").description("사용자 현재 위도"),
                                                        parameterWithName("lon").description("사용자 현재 경도"),
                                                        parameterWithName("category").optional().description("편의시설 카테고리")
                                                )
                                                .responseFields(
                                                        fieldWithPath("facilities").description("편의시설 상세 정보 목록"),
                                                        fieldWithPath("facilities[].id").description("편의시설 id"),
                                                        fieldWithPath("facilities[].name").description("편의시설 이름"),
                                                        fieldWithPath("facilities[].type").description("편의시설 종류"),
                                                        fieldWithPath("facilities[].latitude").description("편의시설 위도"),
                                                        fieldWithPath("facilities[].longitude").description("편의시설 경도")
                                                )
                                                .responseSchema(Schema.schema("FacilitiesResponse"))
                                                .build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("편의시설 상세 조회")
    void findFacility() throws Exception {
        //given
        when(facilityService.findFacility(anyLong()))
                .thenReturn(new FacilityDetailResponse(
                        "잠실역 휠체어리프트",
                        FacilityType.WHEELCHAIR_LIFT,
                        "서울특별시 송파구 올림픽로 지하 265",
                        "잠실쇼핑센터 측 연결계단",
                                Map.ofEntries(
                                        Map.entry("phoneNumber",""),
                                        Map.entry("operator",""),
                                        Map.entry("weekdayOpeningHours",""),
                                        Map.entry("weekendOpeningHours",""),
                                        Map.entry("route","B1-B1"),
                                        Map.entry("turnstile",""),
                                        Map.entry("facilityType",""),
                                        Map.entry("operatingType",""),
                                        Map.entry("capacity",""),
                                        Map.entry("fee",""),
                                        Map.entry("subscription",""),
                                        Map.entry("standardFee",""),
                                        Map.entry("additionalFee","")
                                ),
                        "mobby/images/wheelchair_lift.png"
                ));

        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(DEFAULT_URL+"/{facilityId}", 1L)
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
                                                .tag("편의시설 API")
                                                .description("편의시설 상세 조회")
                                                .pathParameters(
                                                        parameterWithName("facilityId").description("편의시설 id")
                                                )
                                                .responseFields(
                                                        fieldWithPath("name").description("편의시설 이름"),
                                                        fieldWithPath("type").description("편의시설 카테고리"),
                                                        fieldWithPath("address").description("편의시설 주소"),
                                                        fieldWithPath("detailLocation").description("편의시설 위치 상세 설명"),
                                                        fieldWithPath("imageUrl").description("편의시설 이미지 링크"),
                                                        fieldWithPath("additional").description("추가 정보"),
                                                        fieldWithPath("additional.phoneNumber").description("관리자 전화번호"),
                                                        fieldWithPath("additional.operator").description("운영 기관"),
                                                        fieldWithPath("additional.weekdayOpeningHours").description("평일 운영시간"),
                                                        fieldWithPath("additional.weekendOpeningHours").description("주말 운영시간"),
                                                        fieldWithPath("additional.route").description("(엘리베이터, 휠체어리프트) 운행 구간"),
                                                        fieldWithPath("additional.turnstile").description("(지하철 장애인화장실) 개찰구 내부/외부"),
                                                        fieldWithPath("additional.facilityType").description("(장애인 복지시설) 복지시설 종류"),
                                                        fieldWithPath("additional.operatingType").description("(주차장) 운영 구분"),
                                                        fieldWithPath("additional.capacity").description("(주차장) 총 주차면수"),
                                                        fieldWithPath("additional.fee").description("(주차장) 유료/무료 구분"),
                                                        fieldWithPath("additional.subscription").description("(주차장) 월 정기권 금액"),
                                                        fieldWithPath("additional.standardFee").description("(주차장) 기본 주차요금"),
                                                        fieldWithPath("additional.additionalFee").description("(주차장) 추가 요금")
                                                )
                                                .responseSchema(Schema.schema("FacilityDetailResponse"))
                                                .build()
                                )
                        )
                );
    }
}
