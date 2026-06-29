package com.devon.building.service.impl;

import com.devon.building.converter.RentAreaConverter;
import com.devon.building.entity.RentAreaEntity;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.repository.RentAreaRepository;
import com.devon.building.service.RentAreaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RentAreaServiceImpl implements RentAreaService {
    private final RentAreaRepository rentAreaRepository;
    private final RentAreaConverter rentAreaConverter;

    @Override
    public void deleteByBuildingIdIn(List<Long> id) {
        rentAreaRepository.deleteByBuildingIdIn(id);
    }

    @Override
    @Transactional
    public void saveOrUpdateRentArea(Long buildingId, String rentArea) {
        rentAreaRepository.deleteByBuildingId(buildingId);
        if (rentArea == null || rentArea.isBlank()) return;
        String[] areas = rentArea.split(",");
        List<RentAreaEntity> rentAreaEntities = new ArrayList<>();
        for(String value : areas){
            RentAreaEntity rentAreaEntity = rentAreaConverter.toRentAreaEntity(buildingId, Long.valueOf(value.trim()));
            rentAreaEntities.add(rentAreaEntity);
        }
        rentAreaRepository.saveAll(rentAreaEntities);
    }
}
