package com.safer.safer.station.domain.repository;

import com.safer.safer.station.domain.Station;

import java.util.List;

public interface CustomStationRepository {
    void saveAll(List<Station> stations);
}
