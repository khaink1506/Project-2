package com.devon.building.converter;

import com.devon.building.builder.CustomerSearchBuilder;
import com.devon.building.entity.BuildingEntity;
import com.devon.building.entity.CustomerEntity;
import com.devon.building.enums.Status;
import com.devon.building.model.dto.BuildingDTO;
import com.devon.building.model.request.CustomerRequest;
import com.devon.building.model.request.CustomerSearchRequest;
import com.devon.building.model.response.CustomerSearchResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerConverter {

    private final ModelMapper modelMapper;

    public CustomerEntity toCustomerEntity(CustomerRequest createCustomerRequest){
        return modelMapper.map(createCustomerRequest, CustomerEntity.class);
    }

    public CustomerSearchBuilder toCustomerSearchBuilder(CustomerSearchRequest request){
        return CustomerSearchBuilder.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .status(request.getStatus())
                .staffId(request.getStaffId())
                .build();
    }

    public CustomerSearchResponse toCustomerSearchResponse(CustomerEntity customerEntity){
        CustomerSearchResponse response = modelMapper.map(customerEntity, CustomerSearchResponse.class);
        if (customerEntity.getStatus() != null) {
            response.setStatus((customerEntity.getStatus().getStatusName()));
        }
        return response;
    }
    public void updateCustomerEntity(CustomerRequest customerRequest, CustomerEntity customerEntity){
        modelMapper.map(customerRequest, customerEntity);
    }

    public CustomerRequest toCustomerRequest(CustomerEntity customerEntity){
        return modelMapper.map(customerEntity, CustomerRequest.class);
    }
}
