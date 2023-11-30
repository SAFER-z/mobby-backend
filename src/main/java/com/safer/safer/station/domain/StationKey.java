package com.safer.safer.station.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StationKey {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String line;

    @Column(nullable = false)
    private String operator;

    private StationKey(String name, String line, String operator) {
        this.name = name;
        this.line = line;
        this.operator = operator;
    }

    public static StationKey of(String name, String line, String operator) {
        return new StationKey(name, line, operator);
    }
}
