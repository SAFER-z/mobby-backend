package com.safer.safer.station.domain;

import com.safer.safer.common.exception.NoSuchElementException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static com.safer.safer.batch.util.BatchConstant.*;
import static com.safer.safer.common.exception.ExceptionCode.NO_SUCH_OPERATOR_TYPE;

@RequiredArgsConstructor
@Getter
public enum OperatorType {
    서울교통공사(List.of(LINE_1, LINE_2, LINE_3, LINE_4, LINE_5, LINE_6, LINE_7, LINE_8)),
    서울9호선(List.of(LINE_9)),
    코레일(List.of(EAST_LINE, WEST_LINE, GYEONGCHUN_LINE, GYEONGUI_LINE, GYEONGGANG_LINE, SUINBUNDANG_LINE, LINE_1, LINE_3, LINE_4)),
    공항철도(List.of(AIRPORT_LINE)),
    김포골드라인(List.of(GP_GOLD_LINE)),
    의정부경전철(List.of(UIJEONGBU_LINE)),
    남서울경전철(List.of(SILIM_LINE)),
    남양주도시공사(List.of(LINE_4)),
    신분당선(List.of(SINBUNDANG_LINE)),
    우이신설경전철(List.of(UI_LINE)),
    용인경전철(List.of(YONGIN_LINE)),
    인천교통공사(List.of(INCHEON_LINE_1, INCHEON_LINE_2, LINE_7)),
    부산김해경전철(List.of(BG_LINE)),
    부산교통공사(List.of(BUSAN_LINE_1, BUSAN_LINE_2, BUSAN_LINE_3, BUSAN_LINE_4)),
    대구도시철도(List.of(DAEGU_LINE_1, DAEGU_LINE_2, DAEGU_LINE_3)),
    광주도시철도(List.of(GWANGJU_LINE)),
    대전교통공사(List.of(DAEJEON_LINE));

    private final List<String> keywords;

    public static OperatorType from(String type) {
        return Arrays.stream(values())
                .filter(facilityType -> facilityType.name().equals(type))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_OPERATOR_TYPE, type));
    }

    public String getTMapKeyword(String stationName, String line) {
        return this.keywords.size() == 1 ? stationName.concat(keywords.get(0)) :
                stationName.concat(this.keywords.stream()
                        .filter(keyword -> keyword.contains(line))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException(NO_SUCH_OPERATOR_TYPE)));
    }
}
