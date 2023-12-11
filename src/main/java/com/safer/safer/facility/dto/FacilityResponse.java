package com.safer.safer.facility.dto;

import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.domain.FacilityType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.safer.safer.facility.domain.FacilityType.TOILET;

@RequiredArgsConstructor
@Getter
public class FacilityResponse {
    private final Long id;
    private final String name;
    private final FacilityType category;
    private final String address;
    private final double latitude;
    private final double longitude;

    public static FacilityResponse from(Facility facility) {
        return facility.getCategory().equals(TOILET) ? ToiletResponse.from(facility) :
                new FacilityResponse(
                        facility.getId(),
                        facility.getName(),
                        facility.getCategory(),
                        facility.getAddress(),
                        facility.getCoordinate().getX(),
                        facility.getCoordinate().getY()
                );
    }
}
