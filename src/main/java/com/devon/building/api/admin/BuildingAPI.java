package com.devon.building.api.admin;


import com.devon.building.exception.DataBuildingInvalidException;
import com.devon.building.model.dto.AssignBuildingDTO;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.service.AssignmentBuildingService;
import com.devon.building.service.BuildingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class BuildingAPI {

    private final BuildingService buildingService;
    private final AssignmentBuildingService assignBuildingService;

    @GetMapping("/{id}/staff")
    public ResponseEntity<ResponseDTO> loadStaffs(@PathVariable Long id){
        return ResponseEntity.ok(buildingService.loadStaffs(id));
    }
    @PostMapping
    public ResponseEntity<ResponseDTO> createBuilding(@RequestBody @Valid BuildingDTO buildingDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (bindingResult.hasErrors()) {
            responseDTO.setMessage("Validation is failed");
            List<String> details = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage()).toList();
            responseDTO.setDetail(details);
            return ResponseEntity.badRequest().body(responseDTO);
        }
        return ResponseEntity.ok().body(buildingService.createBuilding(buildingDTO));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateBuilding(@RequestBody @Valid BuildingDTO buildingDTO, BindingResult bindingResult) {
        ResponseDTO responseDTO = new ResponseDTO();
        if (bindingResult.hasErrors()) {
            responseDTO.setMessage("Validation failed");
            List<String> details = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage()).toList();
            responseDTO.setDetail(details);
            return ResponseEntity.badRequest().body(responseDTO);
        }
        if(buildingDTO.getId() == null) {
            responseDTO.setMessage("Building id is required for update");
            return ResponseEntity.badRequest().body(responseDTO);
        }
        return ResponseEntity.ok().body(buildingService.updateBuilding(buildingDTO));
    }


    @DeleteMapping("/{ids}")
    public ResponseEntity<ResponseDTO> deleteBuilding(@PathVariable List<Long> ids) {
        if(ids == null || ids.isEmpty() || ids.stream().anyMatch(Objects::isNull)){
            throw new DataBuildingInvalidException("At least one ID is required for deletion");
        }
        return ResponseEntity.ok().body(buildingService.deleteBuilding(ids));
    }

    @PutMapping("/assign")
    public ResponseEntity<ResponseDTO> assignBuilding(@RequestBody @Valid AssignBuildingDTO assignBuildingDTO, BindingResult bindingResult){
        ResponseDTO responseDTO = new ResponseDTO();
        if (bindingResult.hasErrors()) {
            responseDTO.setMessage("Not found");
            List<String> details = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage()).toList();
            responseDTO.setDetail(details);
            return ResponseEntity.badRequest().body(responseDTO);
        }

        responseDTO.setMessage("Building assigned successfully");
        return ResponseEntity.ok().body(assignBuildingService.assignmentBuilding(assignBuildingDTO));
    }
}
