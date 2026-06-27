package com.devon.building.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BuildingDTO {

    Long id;
    @NotBlank(message = "name not be blank")
    String name;
    String street;
    String ward;
    @NotBlank(message = "district not be blank")
    String district;
    String structure;
    Integer numberOfBasement;
    Long floorArea;
    String direction;
    String level;
    @Min(value = 0, message = "Rent price must be greater than or equal to 0")
    Long price;
    String rentPriceDescription;
    String serviceFee;
    String carFee;
    String motoFee;
    String overTimeFee;
    Double brokerageFee;
    String managerName;
    @Pattern(regexp = "^$|^\\d{10}$", message = "Invalid phone number format")
    String managerPhone;
    String rentArea;
    @NotEmpty(message = "Building type is required")
    List<String> typeCode;
}
