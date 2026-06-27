package com.devon.building.service.impl;

import com.devon.building.builder.BuildingSearchBuilder;
import com.devon.building.constant.SystemConstant;
import com.devon.building.converter.BuildingConverter;
import com.devon.building.entity.BuildingEntity;
import com.devon.building.entity.RentAreaEntity;
import com.devon.building.entity.User;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.model.response.BuildingSearchResponse;
import com.devon.building.model.response.StaffResponseDTO;
import com.devon.building.repository.BuildingRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.BuildingService;
import com.devon.building.service.RentAreaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private final RentAreaService rentAreaService;
    private final BuildingConverter buildingConverter;
    private final BuildingRepository buildingRepository;
    private final UserRepository userRepository;

    @Override
    public List<BuildingSearchResponse> findBuilding(BuildingSearchRequest buildingSearchRequest) {
        BuildingSearchBuilder buildingSearchBuilder = buildingConverter.toBuildingSearchBuilder(buildingSearchRequest);
        List<BuildingEntity> buildingEntity = buildingRepository.findALlBuilding(buildingSearchBuilder);
        List<BuildingSearchResponse> responses = new ArrayList<>();
        for(BuildingEntity building : buildingEntity){
            BuildingSearchResponse buildingSearchResponse = buildingConverter.toBuildingResponse(building);
            responses.add(buildingSearchResponse);
        }
        return responses;
    }

    @Override
    @Transactional
    public ResponseDTO createBuilding(BuildingDTO buildingDTO) {
        BuildingEntity buildingEntity = buildingConverter.toBuildingEntity(buildingDTO);
        buildingEntity.setRentType(String.join(", ", buildingDTO.getTypeCode()));
        buildingRepository.save(buildingEntity);
        buildingDTO.setId(buildingEntity.getId());
        rentAreaService.saveOrUpdateRentArea(buildingDTO);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("create successfully");
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO updateBuilding(BuildingDTO buildingDTO){
        buildingRepository.findById(buildingDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Building with id " + buildingDTO.getId() + " not found"));
        BuildingEntity buildingEntity = buildingConverter.toBuildingEntity(buildingDTO);
        buildingEntity.setRentType(String.join(", ", buildingDTO.getTypeCode()));
        buildingRepository.save(buildingEntity);
        rentAreaService.saveOrUpdateRentArea(buildingDTO);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Update successfully");
        responseDTO.setData(buildingConverter.toBuildingDTO(buildingEntity));
        return responseDTO;
    }
    @Override
    @Transactional
    public ResponseDTO deleteBuilding(List<Long> ids) {
        ResponseDTO responseDTO = new ResponseDTO();
        rentAreaService.deleteByBuildings(ids);
        buildingRepository.deleteByIdIn(ids);
        responseDTO.setMessage("Delete successfully");
        return responseDTO;
    }

    @Override
    public BuildingDTO findById(Long id) {
        BuildingEntity buildingEntity = buildingRepository.findById(id).orElseThrow(() -> new RuntimeException("Building id not found"));
        BuildingDTO buildingDTO = buildingConverter.toBuildingDTO(buildingEntity);
        List<RentAreaEntity> rentAreaEntity = buildingEntity.getRentArea();
        String rentArea = rentAreaEntity.stream().map(area -> area.getValue().toString()).collect(Collectors.joining(", "));
        buildingDTO.setRentArea(rentArea);
        buildingDTO.setTypeCode( Arrays.stream(buildingEntity.getRentType().split(", ")).map(String::trim).toList());
        return buildingDTO;
    }

    @Override
    public ResponseDTO loadStaffs(Long buildingId) {
        BuildingEntity buildingEntity = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new RuntimeException("Building id not found"));
        List<User> staffs = userRepository.findAllByUserRoleAndActiveTrue(SystemConstant.STAFF_ROLE);

        Set<Long> assignmentStaffs = buildingEntity.getAssignmentBuilding()
                .stream().map(it -> it.getUser().getId()).collect(Collectors.toSet());

        List<StaffResponseDTO> staffResponseDTOS = new ArrayList<>();
        for(User staff : staffs){
            StaffResponseDTO staffResponseDTO = new StaffResponseDTO();
            staffResponseDTO.setUserName(staff.getUserName());
            staffResponseDTO.setId(staff.getId());
            staffResponseDTO.setChecked(assignmentStaffs.contains(staff.getId()) ? "checked" : " ");
            staffResponseDTOS.add(staffResponseDTO);
        }
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(staffResponseDTOS);
        responseDTO.setMessage("Load staff list successfully");
        return responseDTO;
    }


}
