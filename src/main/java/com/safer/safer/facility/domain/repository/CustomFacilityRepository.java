package com.safer.safer.facility.domain.repository;

import com.safer.safer.facility.domain.Facility;

import java.util.List;

public interface CustomFacilityRepository {

    void saveAll(List<Facility> facilities);

    void updateAll(List<Facility> facilities);
}
