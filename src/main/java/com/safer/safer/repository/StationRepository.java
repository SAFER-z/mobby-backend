package com.safer.safer.repository;

import com.safer.safer.domain.Station;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station,Long> {
    Optional<Station> findByNameContaining(final String stationName);

    Optional<Station> findByNameAndLine(final String name, final String line);

    @Query(value = "select * from Station s where ST_Dwithin(s.coordinate, :coordinate, 1500, false)", nativeQuery = true)
    List<Station> findAllByDistance(@Param("coordinate") Point coordinate);
}
