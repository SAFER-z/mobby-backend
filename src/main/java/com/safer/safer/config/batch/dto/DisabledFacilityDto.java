package com.safer.safer.config.batch.dto;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.domain.util.CsvUtil;
import com.safer.safer.domain.util.TMapUtil;
import com.safer.safer.domain.Facility;
import com.safer.safer.domain.FacilityType;
import lombok.Getter;

@Getter
public class DisabledFacilityDto {
    @CsvBindByPosition(position = 0)
    private String name;
    @CsvBindByPosition(position = 1)
    private String detailType;
    @CsvBindByPosition(position = 2)
    private String type;
    @CsvBindByPosition(position = 3)
    private String address;
    @CsvBindByPosition(position = 4)
    private String phoneNumber;

    public Facility toEntity() {
        return Facility.of(
                name,
                FacilityType.from(type),
                TMapUtil.findPointByKeyword(address),
                address,
                String.join(";",
                        "facilityType="+ CsvUtil.parseParenthesis(detailType),
                        "phoneNumber="+phoneNumber
                )
        );
    }
}
