package com.devon.building.service;

import com.devon.building.model.dto.AssignCustomerDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.ContactRequest;
import com.devon.building.model.request.CustomerRequest;
import com.devon.building.model.request.CustomerSearchRequest;
import com.devon.building.model.response.CustomerSearchResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {

    Page<CustomerSearchResponse> findCustomer(CustomerSearchRequest customerSearchRequest);

    ResponseDTO createCustomer(CustomerRequest request);

    ResponseDTO updateCustomer(CustomerRequest request);

    CustomerRequest findById(Long id);

    ResponseDTO loadStaffs(Long customerId);

    ResponseDTO assignmentCustomer(AssignCustomerDTO assignCustomerDTO);

    ResponseDTO deleteCustomer(List<Long> ids);

    ResponseDTO saveContact(ContactRequest request);
}
