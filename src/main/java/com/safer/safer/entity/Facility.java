package com.safer.safer.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.locationtech.jts.geom.Point;

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

    @Column(columnDefinition = "POINT")
    private Point point;

    private String detailLocation;

    private String additional;

    private String imageUrl;

    @ManyToOne(optional = false)
    private Station station;

    private Facility(String name, FacilityType type, Point point) {
        this.name = name;
        this.type = type;
        this.point = point;
    }

    public static Facility of(String name, FacilityType type, Point point) {
        return new Facility(name, type, point);
    }
}
