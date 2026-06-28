package com.devon.building.service.impl;

import com.devon.building.entity.AssignmentBuilding;
import com.devon.building.entity.BuildingEntity;
import com.devon.building.entity.User;
import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.repository.AssignmentBuildingRepository;
import com.devon.building.repository.BuildingRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.AssignmentBuildingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentBuildingServiceImpl implements AssignmentBuildingService {
    private final BuildingRepository buildingRepository;
    private final AssignmentBuildingRepository assignmentBuildingRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ResponseDTO assignmentBuilding(AssignBuildingDTO assignBuildingDTO){
        BuildingEntity buildingEntity = buildingRepository.findById(assignBuildingDTO.getBuildingId())
                .orElseThrow(() -> new RuntimeException("Building id not found"));
        assignmentBuildingRepository.deleteByBuilding(buildingEntity);
        List<Long> staffIds = assignBuildingDTO.getStaffIds();
        for(Long staff : staffIds){
            AssignmentBuilding assignmentBuilding = new AssignmentBuilding();
            assignmentBuilding.setBuilding(buildingEntity);

            User user = userRepository.findById(staff).orElseThrow(() -> new RuntimeException("User id not found"));
            assignmentBuilding.setUser(user);
            assignmentBuildingRepository.save(assignmentBuilding);
        }
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Assign building successfully");
        return responseDTO;
    }
}
