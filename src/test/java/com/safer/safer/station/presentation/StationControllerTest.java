package com.safer.safer.station.presentation;

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
import com.safer.safer.facility.dto.FacilityDetailResponse;
import com.safer.safer.station.application.StationService;
import com.safer.safer.station.dto.StationDetailResponse;
import com.safer.safer.station.dto.StationResponse;
import com.safer.safer.station.dto.StationsResponse;
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
                                "서울특별시 송파구 올림픽로 지하265(신천동)",
                                "mobby/images/station.png",
                                true,
                                true,
                                true,
                                true,
                                true,
                                true,
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
                                                        fieldWithPath("stations[].address").description("역 주소"),
                                                        fieldWithPath("stations[].imageUrl").description("역 이미지 링크"),
                                                        fieldWithPath("stations[].hasAccessibleRamp").type(Boolean.TYPE).description("이동식 안전발판 유무"),
                                                        fieldWithPath("stations[].hasLift").type(Boolean.TYPE).description("휠체어리프트 유무"),
                                                        fieldWithPath("stations[].hasToilet").type(Boolean.TYPE).description("화장실 유무"),
                                                        fieldWithPath("stations[].hasRamp").type(Boolean.TYPE).description("출입구 경사로 유무"),
                                                        fieldWithPath("stations[].hasElevator").type(Boolean.TYPE).description("엘리베이터 유무"),
                                                        fieldWithPath("stations[].hasCharger").type(Boolean.TYPE).description("전동휠체어 충전기 유무"),
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
                        "02-6110-2161",
                        "mobby/images/station/jamsil.png",
                        true,
                        "1-4, 4-4, 7-1, 10-1",
                        List.of(new FacilityDetailResponse(
                                "잠실역 휠체어리프트",
                                        FacilityType.WHEELCHAIR_LIFT,
                                        "서울특별시 송파구 올림픽로 지하 265",
                                        Map.ofEntries(
                                                Map.entry("route",""),
                                                Map.entry("detailLocation", "")
                                        ),
                                        "mobby/images/wheelchair_lift.png"
                                ),
                                new FacilityDetailResponse(
                                        "잠실역 장애인화장실",
                                        FacilityType.TOILET,
                                        "서울특별시 송파구 올림픽로 지하 265",
                                        Map.ofEntries(
                                                Map.entry("detailLocation", ""),
                                                Map.entry("openingHours", ""),
                                                Map.entry("turnstile", ""),
                                                Map.entry("accessible", "")
                                        ),
                                        "mobby/images/toilet.png"
                                ),
                                new FacilityDetailResponse(
                                        "잠실역 경사로",
                                        FacilityType.ACCESSIBLE_RAMP,
                                        "서울특별시 송파구 올림픽로 지하 265",
                                        Map.ofEntries(Map.entry("detailLocation", "")),
                                        "mobby/images/ramp.png"
                                ),
                                new FacilityDetailResponse(
                                        "잠실역 엘리베이터 1호기",
                                        FacilityType.ELEVATOR,
                                        "서울특별시 송파구 올림픽로 지하 265",
                                        Map.ofEntries(
                                                Map.entry("route",""),
                                                Map.entry("detailLocation", "")
                                        ),
                                        "mobby/images/elevator.png"
                                ),
                                new FacilityDetailResponse(
                                        "잠실역 전동휠체어 고속충전기",
                                        FacilityType.WHEELCHAIR_CHARGER,
                                        "서울특별시 송파구 올림픽로 지하 265",
                                        Map.ofEntries(Map.entry("detailLocation", "")),
                                        "mobby/images/charger.png"
                                )
                        )
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
                                                        fieldWithPath("line").description("노선명"),
                                                        fieldWithPath("address").description("역 주소"),
                                                        fieldWithPath("operator").description("운영기관명"),
                                                        fieldWithPath("phoneNumber").description("역 전화번호"),
                                                        fieldWithPath("imageUrl").description("역 이미지 링크"),
                                                        fieldWithPath("hasAccessibleRamp").type(Boolean.TYPE).description("이동식 안전발판 유무"),
                                                        fieldWithPath("accessibleArea").description("휠체어전용칸 탑승위치"),
                                                        fieldWithPath("facilities").description("편의시설 상세 정보 목록"),
                                                        fieldWithPath("facilities[].name").description("편의시설 이름"),
                                                        fieldWithPath("facilities[].category").description("편의시설 종류"),
                                                        fieldWithPath("facilities[].address").description("편의시설 주소"),
                                                        fieldWithPath("facilities[].detailInfo").description("추가 정보"),
                                                        fieldWithPath("facilities[].detailInfo.detailLocation").description("(전체)역 기준 상세위치"),
                                                        fieldWithPath("facilities[].detailInfo.route").optional().description("(휠체어리프트,엘리베이터)운행구간"),
                                                        fieldWithPath("facilities[].detailInfo.openingHours").optional().description("(화장실)운영시간"),
                                                        fieldWithPath("facilities[].detailInfo.turnstile").optional().description("(화장실)개찰구 내/외"),
                                                        fieldWithPath("facilities[].detailInfo.accessible").optional().description("(화장실)장애인화장실 여부"),
                                                        fieldWithPath("facilities[].imageUrl").description("편의시설 이미지 링크")
                                                )
                                                .responseSchema(Schema.schema("StationDetailResponse"))
                                                .build()
                                )
                        )
                );
    }
}
