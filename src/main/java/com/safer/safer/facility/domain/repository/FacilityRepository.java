package com.safer.safer.facility.domain.repository;

import com.safer.safer.facility.domain.Facility;
import com.safer.safer.facility.dto.FacilityDistanceResponse;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility,Long> {

    @Query(value = "select * from Facility f where ST_Dwithin(f.coordinate, :coordinate, 1500, false)", nativeQuery = true)
    List<Facility> findFacilitiesWithin(@Param("coordinate") Point coordinate);

    @Query(value = "select * from Facility f where ST_Dwithin(f.coordinate, :coordinate, 1500, false) and f.category = :category", nativeQuery = true)
    List<Facility> findFacilitiesByCategoryWithin(@Param("coordinate") Point coordinate, @Param("category") String category);

    @Query(value = "select f.id, f.name, f.address, f.category, round(ST_DistanceSphere(f.coordinate, :coordinate)) as distance " +
            "from Facility f where ST_Dwithin(f.coordinate, :coordinate, 1500, false) and f.category = :category " +
            "order by ST_DistanceSphere(f.coordinate, :coordinate)", nativeQuery = true)
    List<FacilityDistanceResponse> findFacilitiesWithDistance(@Param("coordinate") Point coordinate, @Param("category") String category);

    @Query(value = "select f.id, f.name, f.address, f.category, round(ST_DistanceSphere(f.coordinate, :coordinate)) as distance " +
            "from Facility f where f.name like concat('%',:query,'%') and f.address like concat('%',:query,'%') " +
            "limit 10", nativeQuery = true)
    List<FacilityDistanceResponse> findSearchResult(@Param("query") String query, @Param("coordinate") Point coordinate);
}
