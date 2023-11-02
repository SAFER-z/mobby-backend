package com.safer.safer.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.util.Optional;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityType type;

    @Column(nullable = false)
    private Point coordinate;

    private String address;

    private String detailLocation;

    private String additional;

    @Setter
    private String imageUrl;

    @ManyToOne
    @JoinColumn
    private Station station;

    private Facility(String name, FacilityType type, Point coordinate, String address, String detailLocation, String additional, Station station) {
        this.name = name;
        this.type = type;
        this.coordinate = coordinate;
        this.address = address;
        this.detailLocation = detailLocation;
        this.additional = additional;
        this.station = station;
    }

    public static Facility of(String name, FacilityType type, Point coordinate) {
        return new Facility(name, type, coordinate, null, null, null, null);
    }

    public static Facility of(String name, FacilityType type, Point coordinate, String address, String additional) {
        return new Facility(name, type, coordinate, address, null, additional, null);
    }

    public static Facility of(String name, FacilityType type, Point coordinate, String address, String detailLocation, String additional) {
        return new Facility(name, type, coordinate, address, detailLocation, additional, null);
    }

    public static Facility of(String name, FacilityType type, Point coordinate, String detailLocation, Station station) {
        return new Facility(name, type, coordinate, null, detailLocation, null, station);
    }

    public static Facility of(String name, FacilityType type, String detailLocation, String additional, Point coordinate, Station station) {
        return new Facility(name, type, coordinate, null, detailLocation, additional, station);
    }

    public boolean isInStation() {
        return Optional.ofNullable(station).isPresent();
    }
}
