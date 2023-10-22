package com.safer.safer.config.batch.dto;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.domain.util.TMapUtil;
import com.safer.safer.domain.Facility;
import com.safer.safer.domain.FacilityType;
import lombok.Getter;

@Getter
public class DisabledFacilityDto {
    @CsvBindByPosition(position = 0)
    private String address;
    @CsvBindByPosition(position = 1)
    private String type;
    @CsvBindByPosition(position = 2)
    private String name;
    @CsvBindByPosition(position = 3)
    private String operator;
    @CsvBindByPosition(position = 4)
    private String phoneNumber;
    @CsvBindByPosition(position = 5)
    private String etc;

    public Facility toEntity() {
        return Facility.of(
                name,
                FacilityType.from(type),
                TMapUtil.findPointByAddress(address),
                address,
                String.join(";",
                        "시설운영주체:"+operator,
                        "전화번호:"+phoneNumber,
                        "비고:"+etc)
        );
    }
}
