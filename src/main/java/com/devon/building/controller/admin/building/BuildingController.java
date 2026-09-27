package com.devon.building.controller.admin.building;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.BuildingEntity;
import com.devon.building.entity.User;
import com.devon.building.enums.District;
import com.devon.building.enums.RentType;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.request.BuildingSearchRequest;
import com.devon.building.model.response.BuildingSearchResponse;
import com.devon.building.service.BuildingService;
import com.devon.building.service.UserService;
import com.devon.building.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final UserService userService;
    private final BuildingService buildingService;
    private static final String DISTRICT = "districts";
    private static final String RENT_TYPE = "rentTypes";


    @GetMapping("/list")
    public ModelAndView getAllBuildings(@ModelAttribute BuildingSearchRequest buildingSearchRequest){
        if(SecurityUtils.getAuthorities().contains(SystemConstant.STAFF_ROLE)){
            User user = userService.getUserByUsername(SecurityUtils.getCurrentUsername());
            buildingSearchRequest.setStaffId(user.getId());
        }
        ModelAndView modelAndView = new ModelAndView("admin/building/buildingList");
        modelAndView.addObject("staffs", userService.loadStaff());
        modelAndView.addObject(DISTRICT, District.getDistricMap());
        modelAndView.addObject(RENT_TYPE, RentType.getRentTypeMap());
        modelAndView.addObject("buildingSearchRequest", buildingSearchRequest);
        Page<BuildingSearchResponse> result = buildingService.findBuilding(buildingSearchRequest);
        modelAndView.addObject("result", result);

        return modelAndView;
    }

    @GetMapping("/edit")
    public ModelAndView getEditBuildings(@ModelAttribute("buildingEdit") BuildingDTO buildingDTO){
        ModelAndView modelAndView = new ModelAndView("admin/building/buildingEdit");
        modelAndView.addObject(DISTRICT, District.getDistricMap());
        modelAndView.addObject(RENT_TYPE, RentType.getRentTypeMap());
        modelAndView.addObject("building", new BuildingDTO());
        return modelAndView;
    }

    @GetMapping("/{id}/update")
    public ModelAndView getUpdateBuilding(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("admin/building/buildingEdit");
        if(SecurityUtils.getAuthorities().contains(SystemConstant.STAFF_ROLE)){
            User staff = userService.getUserByUsername(SecurityUtils.getCurrentUsername());
            if(staff.getBuilding().stream().noneMatch(building -> building.getId().equals(id))){
                return new ModelAndView("404");
            }
        }
        BuildingDTO buildingDTO = buildingService.findById(id);
        modelAndView.addObject("building", buildingDTO);
        modelAndView.addObject(DISTRICT, District.getDistricMap());
        modelAndView.addObject(RENT_TYPE, RentType.getRentTypeMap());
        return modelAndView;
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> buildingImage(@RequestParam Long id) {
        BuildingEntity building = buildingService.findEntityById(id);
        if (building.getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(building.getImage());
    }
}
