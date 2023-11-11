package com.safer.safer.batch.dto.facility;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.batch.util.CsvUtil;
import com.safer.safer.batch.util.TMapUtil;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.FacilityType;
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
