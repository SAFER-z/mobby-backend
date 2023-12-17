package com.safer.safer.station.domain.repository;

import com.safer.safer.station.domain.Station;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class StationRepositoryImpl implements CustomStationRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<Station> stations) {
        String sql = "INSERT INTO station (name, line, operator, address, coordinate, phone_number, image_url, ramp_available, accessible_area)"+
                " VALUES (?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.batchUpdate(
                sql,
                stations,
                stations.size(),
                (PreparedStatement ps, Station station) -> {
                    ps.setString(1, station.getStationKey().getName());
                    ps.setString(2, station.getStationKey().getLine());
                    ps.setString(3, station.getStationKey().getOperator());
                    ps.setString(4, station.getAddress());
                    ps.setObject(5, station.getCoordinate(), Types.OTHER);
                    ps.setString(6, station.getPhoneNumber());
                    ps.setString(7, station.getImageUrl());
                    ps.setBoolean(8, station.isRampAvailable());
                    ps.setString(9, station.getAccessibleArea());
                });
    }
}
