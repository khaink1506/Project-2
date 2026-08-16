package com.devon.building.repository;

import com.devon.building.entity.BuildingEntity;
import com.devon.building.repository.custom.BuildingRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BuildingRepository extends JpaRepository<BuildingEntity, Long>,
           /*JpaSpecificationExecutor<BuildingEntity>,*/
        BuildingRepositoryCustom {
}
