package com.devon.building.repository;

import com.devon.building.entity.AssignmentBuilding;
import com.devon.building.entity.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentBuildingRepository extends JpaRepository<AssignmentBuilding, Long> {
    void deleteByBuilding(BuildingEntity building);
    void deleteByBuildingIdIn(List<Long> ids);
}
