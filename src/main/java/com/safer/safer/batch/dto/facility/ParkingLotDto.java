package com.safer.safer.batch.dto.facility;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.batch.util.CsvUtil;
import com.safer.safer.batch.util.GeometryUtil;
import com.safer.safer.common.infrastructure.tmap.TMapRequester;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.safer.safer.batch.util.BatchConstant.SEOUL;
import static com.safer.safer.facility.domain.FacilityType.PARKING_LOT;

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
        Point coordinate = latitude.isBlank() ? TMapRequester.findPointByKeyword(name) :
                 GeometryUtil.getPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));

        return Facility.of(
                CsvUtil.parseParenthesis(name),
                PARKING_LOT,
                coordinate,
                roadAddress.isBlank() ? String.join(" ", SEOUL, address) : roadAddress,
                Stream.of(
                        "operatingType="+operatingType,
                        phoneNumber.isBlank() ? "" : "phoneNumber="+phoneNumber,
                        capacity.isBlank() ? "" : "capacity="+capacity,
                        capacity.isBlank() ? "" : "accessible="+(Integer.parseInt(capacity) > 50 ? "yes" : "no"),
                        "free="+freeOrPaid,
                        "weekdayOpeningHours="+String.join("~", CsvUtil.parseTime(weekdayOpenTime), CsvUtil.parseTime(weekdayCloseTime)),
                        "weekendOpeningHours="+String.join("~", CsvUtil.parseTime(weekendOpenTime), CsvUtil.parseTime(weekendCloseTime)),
                        monthlyPrice.isBlank() ? "" : "subscription="+monthlyPrice,
                        freeOrPaid.equals("무료") ? "" : standardFee.isBlank() ? "" :
                                "standardFee="+standardFee+"/"+standardUnit+"분",
                        freeOrPaid.equals("무료") ? "" : additionalFee.isBlank() ? "" :
                                "additionalFee="+additionalFee+"/"+additionalUnit+"분"
                )
                        .filter(info -> !info.isBlank())
                        .collect(Collectors.joining(";"))
        );
    }
}
