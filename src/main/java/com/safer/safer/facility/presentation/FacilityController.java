package com.safer.safer.facility.presentation;

import com.safer.safer.auth.dto.UserInfo;
import com.safer.safer.auth.presentation.Auth;
import com.safer.safer.facility.application.FacilityReportService;
import com.safer.safer.facility.dto.*;
import com.safer.safer.facility.application.FacilityService;
import com.safer.safer.facility.dto.report.FacilityRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/facilities")
public class FacilityController {
    private final FacilityService facilityService;
    private final FacilityReportService facilityReportService;

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
        return ResponseEntity.ok(facilityService.findFacilitiesWithin(coordinate, category));
    }

    @GetMapping("/sorted")
    public ResponseEntity<FacilitiesDistanceResponse> findAllWithDistance(
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude,
            @RequestParam("category") String category
    ) {
        CoordinateRequest coordinate = CoordinateRequest.of(latitude, longitude);
        return ResponseEntity.ok(facilityService.findFacilitiesWithDistance(coordinate, category));
    }

    @PostMapping
    public ResponseEntity<Void> reportFacility(
            @RequestPart(value = "facilityRequest") FacilityRequest facilityRequest,
            @RequestPart(value = "imageFile") MultipartFile multipartFile,
            @Auth UserInfo userInfo
    ) throws IOException {
        facilityReportService.reportFacility(facilityRequest, multipartFile, userInfo);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{facilityId}")
    public ResponseEntity<Void> reportFacilityUpdate(
            @PathVariable final Long facilityId,
            @RequestPart(value = "facilityRequest") FacilityRequest facilityRequest,
            @RequestPart(value = "imageFile") MultipartFile multipartFile,
            @Auth UserInfo userInfo
    ) throws IOException {
        facilityReportService.reportFacilityUpdate(facilityId, facilityRequest, multipartFile, userInfo);
        return ResponseEntity.ok().build();
    }
}
