package com.devon.building.service.impl;

import com.devon.building.converter.RentAreaConverter;
import com.devon.building.entity.BuildingEntity;
import com.devon.building.entity.RentAreaEntity;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.repository.BuildingRepository;
import com.devon.building.repository.RentAreaRepository;
import com.devon.building.service.RentAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RentAreaServiceImpl implements RentAreaService {

    private final BuildingRepository buildingRepository;
    private final RentAreaRepository rentAreaRepository;
    private final RentAreaConverter rentAreaConverter;
    @Override
    public void deleteByBuildings(List<Long> id) {
        for(Long ids : id){
            BuildingEntity buildingEntity = buildingRepository.findById(ids)
                    .orElseThrow(() -> new RuntimeException("Building id not found" + ids));
            rentAreaRepository.deleteByBuildingId(buildingEntity.getId());
        }
    }

    @Override
    public void saveOrUpdateRentArea(BuildingDTO buildingDTO) {
        BuildingEntity buildingEntity = buildingRepository.findById(buildingDTO.getId())
                        .orElseThrow(() -> new RuntimeException("building not found" + buildingDTO.getId()));
        rentAreaRepository.deleteByBuildingId(buildingEntity.getId());
        String[] rentArea = buildingDTO.getRentArea().split(",");
        for(String value : rentArea){
            RentAreaEntity rentAreaEntity = rentAreaConverter.toRentAreaEntity(buildingDTO, Long.valueOf(value.trim()));
            rentAreaRepository.save(rentAreaEntity);
        }
    }
}
