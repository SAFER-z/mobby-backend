package com.safer.safer.facility.dto;

import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.FacilityType;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class ToiletResponse extends FacilityResponse {

    private final boolean isAccessible;

    private ToiletResponse(Long id, String name, FacilityType category, String address, double latitude, double longitude, boolean isAccessible) {
        super(id, name, category, address, latitude, longitude);
        this.isAccessible = isAccessible;
    }

    public static FacilityResponse from(Facility facility) {
        return new ToiletResponse(
                facility.getId(),
                facility.getName(),
                facility.getCategory(),
                facility.getAddress(),
                facility.getCoordinate().getX(),
                facility.getCoordinate().getY(),
                Arrays.asList(facility.getDetailInfo().split(";"))
                        .contains("accessible=yes")
        );
    }
}
