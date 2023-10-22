package com.safer.safer.repository;

import com.safer.safer.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station,Long> {
    Optional<Station> findByNameContaining(final String stationName);
    Optional<Station> findByNameAndLine(final String name, final String line);
}
