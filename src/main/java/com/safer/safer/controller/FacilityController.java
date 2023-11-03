package com.safer.safer.controller;

import com.safer.safer.dto.CoordinateRequest;
import com.safer.safer.dto.FacilitiesResponse;
import com.safer.safer.dto.FacilityDetailResponse;
import com.safer.safer.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/facilities")
public class FacilityController {
    private final FacilityService facilityService;

    @GetMapping("/{facilityId}")
    public ResponseEntity<FacilityDetailResponse> find(@PathVariable final Long facilityId) {
        return ResponseEntity.ok(facilityService.findFacility(facilityId));
    }

    @GetMapping
    public ResponseEntity<FacilitiesResponse> findAll(
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude,
            @RequestParam(required = false) String category
    ) {
        CoordinateRequest coordinate = CoordinateRequest.of(latitude, longitude);
        return ResponseEntity.ok(facilityService.findFacilitiesByDistance(coordinate, category));
    }
}
