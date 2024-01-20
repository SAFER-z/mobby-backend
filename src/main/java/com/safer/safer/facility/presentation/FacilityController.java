package com.safer.safer.facility.presentation;

import com.safer.safer.auth.dto.UserInfo;
import com.safer.safer.auth.presentation.Auth;
import com.safer.safer.facility.application.FacilityReportService;
import com.safer.safer.facility.dto.*;
import com.safer.safer.facility.application.FacilityService;
import com.safer.safer.facility.dto.report.FacilityReportRequest;
import com.safer.safer.facility.dto.report.SlackMessage;
import com.safer.safer.facility.dto.report.SlackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

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

    @PostMapping("/reports")
    public ResponseEntity<Void> reportCreation(
            @RequestPart(value = "facilityReport") FacilityReportRequest facilityReportRequest,
            @RequestPart(value = "imageFile", required = false) MultipartFile multipartFile,
            @Auth UserInfo userInfo
    ) throws IOException {
        facilityReportService.reportCreation(facilityReportRequest, multipartFile, userInfo);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reports/{facilityId}")
    public ResponseEntity<Void> reportModification(
            @PathVariable final Long facilityId,
            @RequestPart(value = "facilityReport") FacilityReportRequest facilityReportRequest,
            @RequestPart(value = "imageFile", required = false) MultipartFile multipartFile,
            @Auth UserInfo userInfo
    ) throws IOException {
        facilityReportService.reportModification(facilityId, facilityReportRequest, multipartFile, userInfo);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reports/verified")
    public ResponseEntity<SlackResponse> applyReport(@RequestParam Map<String,String> message) {
        SlackMessage messageDto = SlackMessage.from(message);

        return ResponseEntity.ok(facilityReportService.handleFacilityReport(messageDto));
    }
}
