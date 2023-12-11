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
                                "서울특별시 성동구 둘레길 47-5 (성수동1가)",
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
                                                        fieldWithPath("facilities[].category").description("편의시설 종류"),
                                                        fieldWithPath("facilities[].address").description("편의시설 주소"),
                                                        fieldWithPath("facilities[].isAccessible").optional().type(boolean.class).description("(화장실)장애인화장실 여부"),
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
    @DisplayName("주차장 상세 조회")
    void findParkingLot() throws Exception {
        //given
        when(facilityService.findFacility(anyLong()))
                .thenReturn(new FacilityDetailResponse(
                        "강변 공영주차장",
                        FacilityType.PARKING_LOT,
                        "서울특별시 성동구 둘레길 47-5 (성수동1가)",
                                Map.ofEntries(
                                        Map.entry("operatingType",""),
                                        Map.entry("phoneNumber",""),
                                        Map.entry("capacity",""),
                                        Map.entry("accessible",""),
                                        Map.entry("free",""),
                                        Map.entry("weekdayOpeningHours",""),
                                        Map.entry("weekendOpeningHours",""),
                                        Map.entry("subscription",""),
                                        Map.entry("standardFee",""),
                                        Map.entry("additionalFee","")
                                ),
                        "mobby/images/parking_lot.png"
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
                                                .description("편의시설 상세 조회 (주차장)")
                                                .pathParameters(
                                                        parameterWithName("facilityId").description("편의시설 id")
                                                )
                                                .responseFields(
                                                        fieldWithPath("name").description("주차장 이름"),
                                                        fieldWithPath("category").description("편의시설 카테고리"),
                                                        fieldWithPath("address").description("주차장 주소"),
                                                        fieldWithPath("detailInfo").description("상세 정보"),
                                                        fieldWithPath("detailInfo.operatingType").description("운영 구분 (ex.시간제 주차장)"),
                                                        fieldWithPath("detailInfo.phoneNumber").description("관리자 전화번호"),
                                                        fieldWithPath("detailInfo.capacity").description("총 주차면수"),
                                                        fieldWithPath("detailInfo.accessible").description("장애인전용 주차구역 유무"),
                                                        fieldWithPath("detailInfo.free").description("유료/무료 구분"),
                                                        fieldWithPath("detailInfo.weekdayOpeningHours").description("평일 운영시간"),
                                                        fieldWithPath("detailInfo.weekendOpeningHours").description("주말 운영시간"),
                                                        fieldWithPath("detailInfo.subscription").description("월 정기권 금액"),
                                                        fieldWithPath("detailInfo.standardFee").description("기본 주차요금"),
                                                        fieldWithPath("detailInfo.additionalFee").description("추가 요금"),
                                                        fieldWithPath("imageUrl").description("주차장 이미지 주소")
                                                )
                                                .responseSchema(Schema.schema("ParkingLotDetailResponse"))
                                                .build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("전동휠체어 충전소 상세 조회")
    void findCharger() throws Exception {
        //given
        when(facilityService.findFacility(anyLong()))
                .thenReturn(new FacilityDetailResponse(
                        "서울숲공원 커뮤니티센터 전동휠체어 급속충전기",
                        FacilityType.WHEELCHAIR_CHARGER,
                        "서울특별시 성동구 뚝섬로 273",
                        Map.ofEntries(
                                Map.entry("detailLocation",""),
                                Map.entry("phoneNumber",""),
                                Map.entry("weekdayOpeningHours",""),
                                Map.entry("weekendOpeningHours","")
                        ),
                        "mobby/images/charger.png"
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
                                                .description("편의시설 상세 조회 (충전소)")
                                                .pathParameters(
                                                        parameterWithName("facilityId").description("편의시설 id")
                                                )
                                                .responseFields(
                                                        fieldWithPath("name").description("충전소 이름"),
                                                        fieldWithPath("category").description("편의시설 카테고리"),
                                                        fieldWithPath("address").description("충전소 주소"),
                                                        fieldWithPath("detailInfo").description("상세 정보"),
                                                        fieldWithPath("detailInfo.detailLocation").description("(일반,역)설치장소 상세설명"),
                                                        fieldWithPath("detailInfo.phoneNumber").description("(일반)관리기관 전화번호"),
                                                        fieldWithPath("detailInfo.weekdayOpeningHours").description("(일반)평일 운영시간"),
                                                        fieldWithPath("detailInfo.weekendOpeningHours").description("(일반)주말 운영시간"),
                                                        fieldWithPath("imageUrl").description("충전소 이미지 주소")
                                                )
                                                .responseSchema(Schema.schema("ChargerDetailResponse"))
                                                .build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("화장실 상세 조회")
    void findToilet() throws Exception {
        //given
        when(facilityService.findFacility(anyLong()))
                .thenReturn(new FacilityDetailResponse(
                        "서울숲코오롱디지털타워 개방화장실",
                        FacilityType.TOILET,
                        "서울특별시 성동구 성수일로4길 25, 서울숲코오롱디지털타워 (성수동2가)",
                        Map.ofEntries(
                                Map.entry("detailLocation",""),
                                Map.entry("phoneNumber",""),
                                Map.entry("openingHours",""),
                                Map.entry("turnstile",""),
                                Map.entry("accessible","")
                        ),
                        "mobby/images/toilet.png"
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
                                                .description("편의시설 상세 조회 (화장실)")
                                                .pathParameters(
                                                        parameterWithName("facilityId").description("편의시설 id")
                                                )
                                                .responseFields(
                                                        fieldWithPath("name").description("화장실 이름"),
                                                        fieldWithPath("category").description("편의시설 카테고리"),
                                                        fieldWithPath("address").description("화장실 주소"),
                                                        fieldWithPath("detailInfo").description("상세 정보"),
                                                        fieldWithPath("detailInfo.accessible").description("(일반,역)장애인화장실 여부"),
                                                        fieldWithPath("detailInfo.phoneNumber").description("(일반)관리기관 전화번호"),
                                                        fieldWithPath("detailInfo.openingHours").description("(일반,역)운영시간"),
                                                        fieldWithPath("detailInfo.detailLocation").description("(역)화장실 상세위치"),
                                                        fieldWithPath("detailInfo.turnstile").description("(역)개찰구 안/밖"),
                                                        fieldWithPath("imageUrl").description("화장실 이미지 주소")
                                                )
                                                .responseSchema(Schema.schema("ToiletDetailResponse"))
                                                .build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("복지시설 상세 조회")
    void findWelfareFacility() throws Exception {
        //given
        when(facilityService.findFacility(anyLong()))
                .thenReturn(new FacilityDetailResponse(
                        "성동재활의원",
                        FacilityType.WELFARE_FACILITY,
                        "서울특별시 성동구 뚝섬로1길 43, 1층 (성수동1가, 성수문화복지회관)",
                        Map.ofEntries(
                                Map.entry("type",""),
                                Map.entry("phoneNumber","")
                        ),
                        "mobby/images/welfare_facility.png"
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
                                                .description("편의시설 상세 조회 (복지시설)")
                                                .pathParameters(
                                                        parameterWithName("facilityId").description("편의시설 id")
                                                )
                                                .responseFields(
                                                        fieldWithPath("name").description("복지시설 이름"),
                                                        fieldWithPath("category").description("편의시설 카테고리"),
                                                        fieldWithPath("address").description("복지시설 주소"),
                                                        fieldWithPath("detailInfo").description("상세 정보"),
                                                        fieldWithPath("detailInfo.type").description("복지시설 종류"),
                                                        fieldWithPath("detailInfo.phoneNumber").description("복지시설 전화번호"),
                                                        fieldWithPath("imageUrl").description("복지시설 이미지 주소")
                                                )
                                                .responseSchema(Schema.schema("WelfareFacilityDetailResponse"))
                                                .build()
                                )
                        )
                );
    }
}