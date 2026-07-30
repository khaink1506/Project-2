package com.devon.building.repository.custom;

import com.devon.building.builder.BuildingSearchBuilder;
import com.devon.building.entity.BuildingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BuildingRepositoryCustom {
    Page<BuildingEntity> findALlBuilding(BuildingSearchBuilder buildingSearchBuilder, Pageable pageable);
}
