package com.devon.building.service.impl;

import com.devon.building.builder.BuildingSearchBuilder;
import com.devon.building.constant.SystemConstant;
import com.devon.building.converter.BuildingConverter;
import com.devon.building.entity.BuildingEntity;
import com.devon.building.entity.RentAreaEntity;
import com.devon.building.entity.User;
import com.devon.building.exception.InvalidRequestException;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.model.response.BuildingSearchResponse;
import com.devon.building.model.response.StaffResponseDTO;
import com.devon.building.repository.AssignmentBuildingRepository;
import com.devon.building.repository.BuildingRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.service.BuildingService;
import com.devon.building.service.RentAreaService;
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
    private final AssignmentBuildingRepository assignmentBuildingRepository;

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
        BuildingEntity savedBuilding = buildingRepository.save(buildingEntity);
        rentAreaService.saveOrUpdateRentArea(savedBuilding.getId(),buildingDTO.getRentArea());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Tạo tòa nhà thành công");
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO updateBuilding(BuildingDTO buildingDTO){
        if(buildingDTO.getId() == null) {
            throw new InvalidRequestException("Phải có ID tòa nhà cần cập nhật");
        }
        buildingRepository.findById(buildingDTO.getId())
                .orElseThrow(() -> new InvalidRequestException("Không tìm thấy tòa nhà có ID: " + buildingDTO.getId()));
        BuildingEntity buildingEntity = buildingConverter.toBuildingEntity(buildingDTO);
        buildingEntity.setRentType(String.join(", ", buildingDTO.getTypeCode()));
        BuildingEntity savedBuilding =  buildingRepository.save(buildingEntity);
        rentAreaService.saveOrUpdateRentArea(savedBuilding.getId(), buildingDTO.getRentArea());
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Cập nhật tòa nhà thành công");
        responseDTO.setData(buildingConverter.toBuildingDTO(buildingEntity));
        return responseDTO;
    }
    @Override
    @Transactional
    public ResponseDTO deleteBuilding(List<Long> ids) {
        if (ids == null || ids.isEmpty() || ids.contains(null)) {
            throw new InvalidRequestException("Không có ID tòa nhà được cung cấp");
        }
        List<BuildingEntity> buildings = buildingRepository.findAllById(ids);
        if(buildings.size() != ids.size()){
            throw new InvalidRequestException("ID tòa nhà không tồn tại");
        }
        ResponseDTO responseDTO = new ResponseDTO();
        assignmentBuildingRepository.deleteByBuildingIdIn(ids);
        rentAreaService.deleteByBuildingIdIn(ids);
        buildingRepository.deleteAllById(ids);
        responseDTO.setMessage("Xóa tòa nhà thành công");
        return responseDTO;
    }

    @Override
    public BuildingDTO findById(Long id) {
        if(id == null){
            throw new InvalidRequestException("ID không được để trống");
        }
        BuildingEntity buildingEntity = buildingRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Không tìm thấy toà nhà có ID: " + id));
        BuildingDTO buildingDTO = buildingConverter.toBuildingDTO(buildingEntity);
        List<RentAreaEntity> rentAreaEntity = buildingEntity.getRentArea();
        String rentArea = rentAreaEntity.stream()
                .map(area -> String.valueOf(area.getValue()))
                .collect(Collectors.joining(", "));
        buildingDTO.setRentArea(rentArea);
        buildingDTO.setTypeCode( Arrays.stream(buildingEntity.getRentType().split(", ")).map(String::trim).toList());
        return buildingDTO;
    }

    @Override
    public ResponseDTO loadStaffs(Long buildingId) {
        BuildingEntity buildingEntity = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new InvalidRequestException("Không tìm thầy tòa nhà có ID: " + buildingId));
        List<User> staffs = userRepository.findAllByUserRoleAndActiveTrue(SystemConstant.STAFF_ROLE);
        Set<Long> assignmentStaffs = buildingEntity.getAssignmentBuilding()
                .stream().map(it -> it.getUser().getId()).collect(Collectors.toSet());
        List<StaffResponseDTO> staffResponseDTOs = new ArrayList<>();
        for(User staff : staffs){
            StaffResponseDTO staffResponseDTO = new StaffResponseDTO();
            staffResponseDTO.setUserName(staff.getUserName());
            staffResponseDTO.setId(staff.getId());
            staffResponseDTO.setChecked(assignmentStaffs.contains(staff.getId()) ? "checked" : "");
            staffResponseDTOs.add(staffResponseDTO);
        }
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(staffResponseDTOs);
        responseDTO.setMessage("Hiển tên nhân viên thành công");
        return responseDTO;
    }
}
