package com.devon.building.converter;

import com.devon.building.entity.BuildingEntity;
import com.devon.building.entity.RentAreaEntity;
import com.devon.building.model.dto.BuildingDTO;
import org.springframework.stereotype.Component;

@Component
public class RentAreaConverter {
//    public RentAreaEntity toRentAreaEntity(BuildingDTO buildingDTO, Long value){
//        RentAreaEntity rentAreaEntity = new RentAreaEntity();
//        rentAreaEntity.setValue(value);
//        BuildingEntity buildingEntity = new BuildingEntity();
//        buildingEntity.setId(buildingDTO.getId());
//        rentAreaEntity.setBuilding(buildingEntity);
//        return rentAreaEntity;
//    }

    public RentAreaEntity toRentAreaEntity(Long buildingId, Long value){
        RentAreaEntity rentAreaEntity = new RentAreaEntity();
        rentAreaEntity.setValue(value);
        BuildingEntity buildingEntity = new BuildingEntity();
        buildingEntity.setId(buildingId);
        rentAreaEntity.setBuilding(buildingEntity);
        return rentAreaEntity;
    }
}
