package com.safer.safer.batch.dto.facility;

import com.opencsv.bean.CsvBindByPosition;
import com.safer.safer.batch.util.GeometryUtil;
import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.FacilityType;

public class ToiletDto {
    @CsvBindByPosition(position = 0)
    private String name;
    @CsvBindByPosition(position = 1)
    private String detailName;
    @CsvBindByPosition(position = 2)
    private String longitude;
    @CsvBindByPosition(position = 3)
    private String latitude;

    public Facility toEntity() {
        return Facility.of(
                detailName.isBlank() ? name : name.concat(" "+detailName),
                FacilityType.TOILET,
                GeometryUtil.getPoint(Double.parseDouble(latitude), Double.parseDouble(longitude))
        );
    }
}
