package com.safer.safer.domain;

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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String line;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String operator;

    private String imageUrl;

    @Column(nullable = false)
    private Point coordinate;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final Set<Facility> facilities = new LinkedHashSet<>();

    private Station(String name, String line, String operator, String address, Point coordinate) {
        this.name = name;
        this.line = line;
        this.operator = operator;
        this.address = address;
        this.coordinate = coordinate;
    }

    public static Station of(String name, String line, String operator, String address, Point coordinate) {
        return new Station(name, line, operator, address, coordinate);
    }
}
