package com.safer.safer.routing.dto.address;

public record AddressResponse(
        String address
) {
    public static AddressResponse of(String address) {
        return new AddressResponse(address);
    }
}
