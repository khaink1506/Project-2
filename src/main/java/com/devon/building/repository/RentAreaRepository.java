package com.devon.building.repository;

import com.devon.building.entity.RentAreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentAreaRepository extends JpaRepository<RentAreaEntity, Long> {

    void deleteByBuildingId(Long id);
}
