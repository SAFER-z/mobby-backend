package com.safer.safer.repository;

import com.safer.safer.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station,Long> {
    Optional<Station> findByNameContaining(final String stationName);

    @Query("SELECT s FROM Station s WHERE s.name LIKE %:name% AND s.line LIKE %:line%")
    Optional<Station> findByNameAndLine(@Param("name") String name, @Param("line") String line);

    @Query("SELECT s FROM Station s WHERE s.name LIKE %:name% AND s.operator LIKE %:operator%")
    Optional<Station> findByNameAndOperator(@Param("name") String name, @Param("operator") String operator);
}
