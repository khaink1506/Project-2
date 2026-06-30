package com.devon.building.service;

import java.util.List;
public interface RentAreaService {

    void deleteByBuildingIdIn(List<Long> id);

    void saveOrUpdateRentArea(Long buildingId, String rentArea);
}
