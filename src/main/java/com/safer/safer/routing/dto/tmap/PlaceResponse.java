package com.safer.safer.routing.dto.tmap;

import static com.safer.safer.routing.dto.tmap.SearchResult.Place;

public record PlaceResponse(
        String id,
        String name,
        double latitude,
        double longitude,
        String address,
        double distance
) {
    public static PlaceResponse from(Place place) {
        return new PlaceResponse(
                place.getId(),
                place.getName(),
                place.getFrontLat(),
                place.getFrontLon(),
                String.join(" ",
                        place.getNewAddressList().getNewAddress().get(0).getFullAddressRoad(),
                        place.getDetailAddrName()
                ),
                place.getRadius()
        );
    }
}
