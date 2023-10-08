package com.safer.safer.entity;

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
    private String line;

    @Column(nullable = false)
    private String name;

    private String imageUrl;

    @Column(nullable = false, columnDefinition = "POINT")
    private Point point;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final Set<Facility> facilities = new LinkedHashSet<>();


}
