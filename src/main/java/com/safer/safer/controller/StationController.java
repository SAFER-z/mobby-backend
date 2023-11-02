package com.safer.safer.controller;

import com.safer.safer.dto.CoordinateRequest;
import com.safer.safer.dto.StationDetailResponse;
import com.safer.safer.dto.StationsResponse;
import com.safer.safer.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/stations")
public class StationController {
    private final StationService stationService;

    @GetMapping("/{stationId}")
    public ResponseEntity<StationDetailResponse> find(@PathVariable final Long stationId) {
        return ResponseEntity.ok(stationService.findStation(stationId));
    }

    @GetMapping
    public ResponseEntity<StationsResponse> findAll(@RequestBody CoordinateRequest coordinate) {
        return ResponseEntity.ok(stationService.findStationsByDistance(coordinate));
    }

}
