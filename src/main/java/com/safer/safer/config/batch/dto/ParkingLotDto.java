package com.safer.safer.config.batch.dto;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.domain.Facility;
import com.safer.safer.domain.FacilityType;
import com.safer.safer.domain.util.CsvUtil;
import com.safer.safer.domain.util.GeometryUtil;
import com.safer.safer.domain.util.TMapUtil;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.safer.safer.config.batch.tasklet.Constant.SEOUL;

@Getter
public class ParkingLotDto {
    @CsvBindByPosition(position = 0)
    private String name;
    @CsvBindByPosition(position = 1)
    private String roadAddress;
    @CsvBindByPosition(position = 2)
    private String address;
    @CsvBindByPosition(position = 3)
    private String operatingType;
    @CsvBindByPosition(position = 4)
    private String phoneNumber;
    @CsvBindByPosition(position = 5)
    private String capacity;
    @CsvBindByPosition(position = 6)
    private String freeOrPaid;
    @CsvBindByPosition(position = 7)
    private String weekdayOpenTime;
    @CsvBindByPosition(position = 8)
    private String weekdayCloseTime;
    @CsvBindByPosition(position = 9)
    private String weekendOpenTime;
    @CsvBindByPosition(position = 10)
    private String weekendCloseTime;
    @CsvBindByPosition(position = 11)
    private String monthlyPrice;
    @CsvBindByPosition(position = 12)
    private String standardFee;
    @CsvBindByPosition(position = 13)
    private String standardUnit;
    @CsvBindByPosition(position = 14)
    private String additionalFee;
    @CsvBindByPosition(position = 15)
    private String additionalUnit;
    @CsvBindByPosition(position = 16)
    private String latitude;
    @CsvBindByPosition(position = 17)
    private String longitude;

    public Facility toEntity() {
        Point coordinate = latitude.isBlank() ? TMapUtil.findPointByKeyword(name) :
                 GeometryUtil.getPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));

        return Facility.of(
                CsvUtil.parseParenthesis(name),
                FacilityType.PARKING_LOT,
                coordinate,
                roadAddress.isBlank() ? String.join(" ", SEOUL, address) : roadAddress,
                Stream.of(
                        "운영구분="+operatingType,
                        phoneNumber.isBlank() ? "" : "전화번호="+phoneNumber,
                        capacity.isBlank() ? "" : "주차공간="+capacity,
                        "유료/무료="+freeOrPaid,
                        "평일 운영시간="+String.join("~", CsvUtil.parseTime(weekdayOpenTime), CsvUtil.parseTime(weekdayCloseTime)),
                        "주말 운영시간="+String.join("~", CsvUtil.parseTime(weekendOpenTime), CsvUtil.parseTime(weekendCloseTime)),
                        monthlyPrice.isBlank() ? "" : "월 정기권 금액="+monthlyPrice,
                        freeOrPaid.equals("무료") ? "" : standardFee.isBlank() ? "" :
                                "기본주차요금="+standardFee+"/"+standardUnit+"분",
                        freeOrPaid.equals("무료") ? "" : additionalFee.isBlank() ? "" :
                                "추가주차요금="+additionalFee+"/"+additionalUnit+"분"
                )
                        .filter(info -> !info.isBlank())
                        .collect(Collectors.joining(";"))
        );
    }
}
