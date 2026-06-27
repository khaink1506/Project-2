package com.devon.building.service;

import com.devon.building.model.dto.BuildingDTO;

import java.util.List;

public interface RentAreaService {

    public void deleteByBuildings(List<Long> id);

    public void saveOrUpdateRentArea(BuildingDTO buildingDTO);
}
