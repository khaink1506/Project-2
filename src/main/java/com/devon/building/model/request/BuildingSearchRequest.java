package com.devon.building.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingSearchRequest {
    String name;
    Long floorArea;
    String district;
    String ward;
    String street;
    Integer numberOfBasement;
    String direction;
    String level;
    Long areaFrom;
    Long areaTo;
    Long rentPriceFrom;
    Long rentPriceTo;
    String managerName;
    String managerPhone;
    Long staffId;
    List<String> typeCode;

    @Min(1)
    Integer page = 1;

//    @Min(1)
//    @Max(100)
//    Integer size = 10;

}
