package com.devon.building.controller.admin.building;

import com.devon.building.constant.SystemConstant;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
        ModelAndView modelAndView = new ModelAndView("admin/building/buildingList");

        if(SecurityUtils.getAuthorities().contains(SystemConstant.STAFF_ROLE)){
            User user = userService.getUserInfo(SecurityUtils.getCurrentUsername());
            buildingSearchRequest.setStaffId(user.getId());
        }
        modelAndView.addObject("staffs", userService.loadStaff());
        modelAndView.addObject(DISTRICT, District.getDistricMap());
        modelAndView.addObject(RENT_TYPE, RentType.getRentTypeMap());
        modelAndView.addObject("buildingSearchRequest", buildingSearchRequest);
        List<BuildingSearchResponse> responses = buildingService.findBuilding(buildingSearchRequest);
        modelAndView.addObject("buildingList", responses);

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
        BuildingDTO buildingDTO = buildingService.findById(id);
        modelAndView.addObject("building", buildingDTO);
        modelAndView.addObject(DISTRICT, District.getDistricMap());
        modelAndView.addObject(RENT_TYPE, RentType.getRentTypeMap());
        return modelAndView;
    }
}
