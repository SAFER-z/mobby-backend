package com.safer.safer.batch.dto.facility;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.batch.util.CsvUtil;
import com.safer.safer.batch.util.GeometryUtil;
import com.safer.safer.common.infrastructure.tmap.TMapRequester;
import com.safer.safer.facility.domain.Facility;
import org.locationtech.jts.geom.Point;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.safer.safer.batch.util.BatchConstant.NORMAL_TOILET;
import static com.safer.safer.facility.domain.FacilityType.TOILET;

public class ToiletDto {
    @CsvBindByPosition(position = 0)
    private String type;
    @CsvBindByPosition(position = 1)
    private String name;
    @CsvBindByPosition(position = 2)
    private String address;
    @CsvBindByPosition(position = 3)
    private String maleAccessibleToilet;
    @CsvBindByPosition(position = 4)
    private String accessibleUrinal;
    @CsvBindByPosition(position = 5)
    private String femaleAccessibleToilet;
    @CsvBindByPosition(position = 6)
    private String phoneNumber;
    @CsvBindByPosition(position = 7)
    private String openingHours;
    @CsvBindByPosition(position = 8)
    private String latitude;
    @CsvBindByPosition(position = 9)
    private String longitude;

    public Facility toEntity() {
        Point coordinate = latitude.isBlank() ? TMapRequester.findPointByKeyword(address) :
                GeometryUtil.getPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));

        return Facility.of(
                name.contains(NORMAL_TOILET) ? name : name.concat(" "+type),
                TOILET,
                coordinate,
                address,
                Stream.of(
                        phoneNumber.isBlank() ? "" : "phoneNumber="+phoneNumber,
                        "openingHours="+openingHours,
                        "accessible=".concat(CsvUtil.isAccessible(maleAccessibleToilet, accessibleUrinal, femaleAccessibleToilet) ? "yes" : "no")
                        )
                        .filter(info -> !info.isBlank())
                        .collect(Collectors.joining(";"))
        );
    }
}
