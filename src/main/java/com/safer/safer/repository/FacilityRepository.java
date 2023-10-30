package com.safer.safer.repository;

import com.safer.safer.domain.Facility;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility,Long> {
    @Query(value = "select * from Facility f where ST_Dwithin(f.coordinate, :coordinate, 3000, false)", nativeQuery = true)
    List<Facility> findAllByDistance(@Param("coordinate") Point coordinate);

    @Query(value = "select * from Facility f where ST_Dwithin(f.coordinate, :coordinate, 3000, false) and f.type = :type", nativeQuery = true)
    List<Facility> findAllByDistanceAndType(@Param("coordinate") Point coordinate, @Param("type") String type);
}
