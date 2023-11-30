package com.safer.safer.station.domain;

import com.safer.safer.facility.domain.Facility;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    private StationKey stationKey;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Point coordinate;

    @Column(nullable = false)
    private String phoneNumber;

    private String imageUrl;

    private boolean rampAvailable;

    private String accessibleArea;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final Set<Facility> facilities = new LinkedHashSet<>();

    private Station(StationKey stationKey, String address, Point coordinate, String phoneNumber, boolean rampAvailable, String accessibleArea) {
        this.stationKey = stationKey;
        this.address = address;
        this.coordinate = coordinate;
        this.phoneNumber = phoneNumber;
        this.rampAvailable = rampAvailable;
        this.accessibleArea = accessibleArea;
    }

    public static Station of(StationKey stationKey, String address, Point coordinate, String phoneNumber, boolean rampAvailable, String accessibleArea) {
        return new Station(stationKey, address, coordinate, phoneNumber, rampAvailable, accessibleArea);
    }

    public static Station of(StationKey stationKey, String address, Point coordinate, String phoneNumber, boolean rampAvailable) {
        return new Station(stationKey, address, coordinate, phoneNumber, rampAvailable, null);
    }
}
