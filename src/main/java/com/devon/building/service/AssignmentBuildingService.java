package com.devon.building.service;

import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.ResponseDTO;

public interface AssignmentBuildingService {
    ResponseDTO assignmentBuilding(AssignBuildingDTO assignBuildingDTO);
}
