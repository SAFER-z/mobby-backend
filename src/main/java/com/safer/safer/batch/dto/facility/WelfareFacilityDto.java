package com.safer.safer.batch.dto.facility;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.common.infrastructure.tmap.TMapRequester;
import com.safer.safer.facility.domain.Facility;
import lombok.Getter;

import static com.safer.safer.facility.domain.FacilityType.WELFARE_FACILITY;

@Getter
public class WelfareFacilityDto {
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
                WELFARE_FACILITY,
                TMapRequester.findPointByKeyword(address),
                address,
                String.join(";",
                        "type="+type+"-"+detailType,
                        "phoneNumber="+phoneNumber
                )
        );
    }
}
