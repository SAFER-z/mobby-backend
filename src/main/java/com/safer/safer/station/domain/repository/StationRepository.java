package com.safer.safer.station.domain.repository;

import com.safer.safer.station.domain.Station;
import com.safer.safer.station.domain.StationKey;
import com.safer.safer.station.dto.StationDistanceResponse;
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
    List<Station> findStationsWithin(@Param("coordinate") Point coordinate);

    @Query(value = "select s.id, s.name, s.address, s.line, round(ST_DistanceSphere(s.coordinate, :coordinate)) as distance " +
            "from Station s where s.name like concat('%',:query,'%') or s.address like concat('%',:query,'%') or s.line like concat('%',:query,'%')" +
            "limit 5", nativeQuery = true)
    List<StationDistanceResponse> findSearchResult(@Param("query") String query, @Param("coordinate") Point coordinate);
}
