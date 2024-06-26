package com.safer.safer.routing.presentation;

import com.safer.safer.routing.application.RoutingService;
import com.safer.safer.routing.dto.SearchResponse;
import com.safer.safer.routing.dto.tmap.PlaceDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(routingService.searchByKeyword(query, latitude, longitude));
    }

    @GetMapping("places/{placeId}")
    public ResponseEntity<PlaceDetailResponse> find(@PathVariable String placeId) {
        return ResponseEntity.ok(routingService.findPlace(placeId));
    }
}
