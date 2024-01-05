package com.safer.safer.routing.presentation;

import com.safer.safer.facility.application.FacilityService;
import com.safer.safer.facility.dto.CoordinateRequest;
import com.safer.safer.facility.dto.FacilitiesDistanceResponse;
import com.safer.safer.routing.application.RoutingService;
import com.safer.safer.routing.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/routing")
public class RoutingController {

    private final RoutingService routingService;

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(
            @RequestParam("q") String query,
            @RequestParam("lat") double latitude,
            @RequestParam("lon") double longitude
    ) {
        CoordinateRequest coordinate = CoordinateRequest.of(latitude, longitude);
        // TODO: Station과 Address 검색 결과 합쳐서 반환하기
        return ResponseEntity.ok(routingService.searchByKeyword(query, coordinate));
    }
}
