package com.safer.safer.routing.dto.tmap;

import static com.safer.safer.routing.dto.tmap.POIResponse.POI;

public record TMapResponse(
        String id,
        String name,
        double latitude,
        double longitude,
        String address,
        double distance
) {
    public static TMapResponse from(POI poi) {
        return new TMapResponse(
                poi.getId(),
                poi.getName(),
                poi.getFrontLat(),
                poi.getFrontLon(),
                String.join(" ",
                        poi.getNewAddressList().getNewAddress().get(0).getFullAddressRoad(),
                        poi.getDetailAddrName()
                ),
                poi.getRadius()
        );
    }
}
