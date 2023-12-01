package com.safer.safer.facility.domain;

import com.safer.safer.station.domain.Station;
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
    private FacilityType category;

    @Column(nullable = false)
    private Point coordinate;

    private String address;

    private String detailInfo;

    @Setter
    private String imageUrl;

    @ManyToOne
    @JoinColumn
    private Station station;

    private Facility(String name, FacilityType category, Point coordinate, String address, String detailInfo, Station station) {
        this.name = name;
        this.category = category;
        this.coordinate = coordinate;
        this.address = address;
        this.detailInfo = detailInfo;
        this.station = station;
    }

    public static Facility of(String name, FacilityType category, Point coordinate, String address, String detailInfo) {
        return new Facility(name, category, coordinate, address, detailInfo, null);
    }

    public static Facility of(String name, FacilityType category, Point coordinate, String detailInfo, Station station) {
        return new Facility(name, category, coordinate, station.getAddress(), detailInfo, station);
    }

    public boolean isInStation() {
        return Optional.ofNullable(station).isPresent();
    }
}
