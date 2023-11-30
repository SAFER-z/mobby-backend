package com.safer.safer.station.domain.repository;

import com.safer.safer.station.domain.Station;
import com.safer.safer.station.domain.StationKey;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station,Long> {
    Optional<Station> findByStationKey(@Param("stationKey")StationKey stationKey);

    @Query(value = "select * from Station s where ST_Dwithin(s.coordinate, :coordinate, 1500, false)", nativeQuery = true)
    List<Station> findAllByDistance(@Param("coordinate") Point coordinate);
}
