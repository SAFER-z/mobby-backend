package com.safer.safer.controller;

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
import com.safer.safer.domain.FacilityType;
import com.safer.safer.dto.*;
import com.safer.safer.service.FacilityService;
import com.safer.safer.service.StationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;

@WebMvcTest(StationController.class)
public class StationControllerTest extends ControllerTest {

    private static final String DEFAULT_URL = "/api/stations";

    @MockBean
    StationService stationService;

    @Test
    @DisplayName("1.5km 반경 내 역 전체 조회")
    void findStationsByDistance() throws Exception {
        //given
        when(stationService.findStationsByDistance(coordinate))
                .thenReturn(StationsResponse.of(
                        List.of(new StationResponse(
                                1L,
                                "잠실역",
                                "2호선",
                                37.51332367,
                                127.10017812
                        )))
                );
        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(DEFAULT_URL)
                                .param("lat", "37.5448467")
                                .param("lon", "127.0392661")
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
                                                .tag("역 API")
                                                .summary("역 전체 조회")
                                                .description("1.5km 반경 내 역 전체 조회")
                                                .queryParameters(
                                                        parameterWithName("lat").description("사용자 현재 위도"),
                                                        parameterWithName("lon").description("사용자 현재 경도")
                                                )
                                                .responseFields(
                                                        fieldWithPath("stations").description("역 정보 목록"),
                                                        fieldWithPath("stations[].id").description("역 id"),
                                                        fieldWithPath("stations[].name").description("역사명"),
                                                        fieldWithPath("stations[].line").description("노선명"),
                                                        fieldWithPath("stations[].latitude").description("역 위도"),
                                                        fieldWithPath("stations[].longitude").description("역 경도")
                                                )
                                                .responseSchema(Schema.schema("StationsResponse"))
                                                .build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("역 상세 조회")
    void findStation() throws Exception {
        //given
        when(stationService.findStation(anyLong()))
                .thenReturn(new StationDetailResponse(
                        "잠실역",
                        "서울특별시 송파구 올림픽로 지하 265",
                        "2호선",
                        "서울교통공사",
                        "mobby/images/station/jamsil.png",
                        true,
                        true,
                        true,
                        true,
                        false,
                        List.of(new FacilityDetailResponse(
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
                                        Map.entry("turnstile","")
                                ),
                                "mobby/images/wheelchair_lift.png"
                        ))
               ));

        //then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.get(DEFAULT_URL+"/{stationId}", 1L)
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
                                                .tag("역 API")
                                                .description("역 상세 조회")
                                                .pathParameters(
                                                        parameterWithName("stationId").description("역 id")
                                                )
                                                .responseFields(
                                                        fieldWithPath("name").description("역사명"),
                                                        fieldWithPath("address").description("역 주소"),
                                                        fieldWithPath("line").description("노선명"),
                                                        fieldWithPath("operator").description("운영기관명"),
                                                        fieldWithPath("imageUrl").description("역 이미지 링크"),
                                                        fieldWithPath("hasLift").type(Boolean.TYPE).description("휠체어리프트 존재 여부"),
                                                        fieldWithPath("hasToilet").type(Boolean.TYPE).description("장애인화장실 존재 여부"),
                                                        fieldWithPath("hasRamp").type(Boolean.TYPE).description("입구 경사로 존재 여부"),
                                                        fieldWithPath("hasElevator").type(Boolean.TYPE).description("엘리베이터 존재 여부"),
                                                        fieldWithPath("hasCharger").type(Boolean.TYPE).description("전동휠체어 급속충전기 존재 여부"),
                                                        fieldWithPath("facilities").description("역사 내 편의시설 상세 정보 목록"),
                                                        fieldWithPath("facilities[].name").description("편의시설 이름"),
                                                        fieldWithPath("facilities[].type").description("편의시설 카테고리"),
                                                        fieldWithPath("facilities[].address").description("편의시설 주소"),
                                                        fieldWithPath("facilities[].detailLocation").description("편의시설 위치 상세 설명"),
                                                        fieldWithPath("facilities[].imageUrl").description("편의시설 이미지 링크"),
                                                        fieldWithPath("facilities[].additional").description("추가 정보"),
                                                        fieldWithPath("facilities[].additional.phoneNumber").description("관리자 전화번호"),
                                                        fieldWithPath("facilities[].additional.operator").description("운영 기관"),
                                                        fieldWithPath("facilities[].additional.weekdayOpeningHours").description("평일 운영시간"),
                                                        fieldWithPath("facilities[].additional.weekendOpeningHours").description("주말 운영시간"),
                                                        fieldWithPath("facilities[].additional.route").description("(엘리베이터, 휠체어리프트) 운행 구간"),
                                                        fieldWithPath("facilities[].additional.turnstile").description("(지하철 장애인화장실) 개찰구 내부/외부")
                                                )
                                                .responseSchema(Schema.schema("StationDetailResponse"))
                                                .build()
                                )
                        )
                );
    }
}
