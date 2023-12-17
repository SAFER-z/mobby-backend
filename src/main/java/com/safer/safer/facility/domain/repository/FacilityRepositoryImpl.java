package com.safer.safer.facility.domain.repository;

import com.safer.safer.facility.domain.Facility;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class FacilityRepositoryImpl implements CustomFacilityRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveAll(List<Facility> facilities) {
        String sql = "INSERT INTO facility (name, address, category, coordinate, detail_info, image_url, station_id)"+
                " VALUES (?,?,?,?,?,?,?)";
        jdbcTemplate.batchUpdate(
                sql,
                facilities,
                facilities.size(),
                (PreparedStatement ps, Facility facility) -> {
                    ps.setString(1, facility.getName());
                    ps.setString(2, facility.getAddress());
                    ps.setString(3, facility.getCategory().name());
                    ps.setObject(4, facility.getCoordinate(), Types.OTHER);
                    ps.setString(5, facility.getDetailInfo());
                    ps.setString(6, facility.getImageUrl());
                    if (facility.isInStation())
                        ps.setLong(7, facility.getStation().getId());
                    else
                        ps.setNull(7, Types.NULL);
                });
    }

    @Override
    public void updateAll(List<Facility> facilities) {
        //TODO
    }
}
