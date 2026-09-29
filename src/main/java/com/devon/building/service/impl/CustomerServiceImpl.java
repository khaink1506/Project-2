package com.devon.building.service.impl;

import com.devon.building.builder.CustomerSearchBuilder;
import com.devon.building.constant.SystemConstant;
import com.devon.building.converter.CustomerConverter;
import com.devon.building.entity.CustomerEntity;
import com.devon.building.entity.User;
import com.devon.building.enums.Status;
import com.devon.building.exception.DuplicateResourceException;
import com.devon.building.exception.InvalidRequestException;
import com.devon.building.exception.ResourceNotFoundException;
import com.devon.building.model.dto.AssignCustomerDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.ContactRequest;
import com.devon.building.model.request.CustomerRequest;
import com.devon.building.model.request.CustomerSearchRequest;
import com.devon.building.model.response.CustomerSearchResponse;
import com.devon.building.model.response.StaffResponseDTO;
import com.devon.building.repository.CustomerRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.repository.specification.customer.CustomerSpecification;
import com.devon.building.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerConverter customerConverter;
    private final UserRepository userRepository;

    @Override
    public Page<CustomerSearchResponse> findCustomer(CustomerSearchRequest request) {
        CustomerSearchBuilder customerSearchBuilder = customerConverter.toCustomerSearchBuilder(request);
        Pageable pageable = PageRequest.of(request.getPage() - 1,
                SystemConstant.MAX_PAGE_ITEM,
                Sort.by(Sort.Direction.DESC,
                        "createdDate"));
        Specification<CustomerEntity> specification = CustomerSpecification.filter(customerSearchBuilder);
        Page<CustomerEntity> customerPage = customerRepository.findAll(specification, pageable);
        return customerPage.map(customerConverter::toCustomerSearchResponse);
    }

    @Override
    @Transactional
    public ResponseDTO createCustomer(CustomerRequest request) {
        if(customerRepository.existsByPhoneAndIsActiveTrue(request.getPhone())){
            throw new DuplicateResourceException("Số điện thoại đã tồn tại");
        }
        CustomerEntity customerEntity = customerConverter.toCustomerEntity(request);
        customerEntity.setActive(true);
        customerRepository.save(customerEntity);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Thêm khách hàng thành công");
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO updateCustomer(CustomerRequest request) {
        CustomerEntity customerEntity = customerRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id của khách hàng"));
        if(customerRepository.existsByPhoneAndIsActiveTrueAndIdNot(request.getPhone(), request.getId())){
            throw new DuplicateResourceException("Số điện thoại đã tồn tại");
        }
        customerConverter.updateCustomerEntity(request, customerEntity);
        customerRepository.save(customerEntity);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Cập nhật thông tin khách hàng thành công");
        return responseDTO;
    }

    @Override
    public CustomerRequest findById(Long id) {
        if (id == null) {
            throw new InvalidRequestException("ID không được để trống");
        }
        CustomerEntity customerEntity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy customer có ID: " + id));
        return customerConverter.toCustomerRequest(customerEntity);
    }

    @Override
    public ResponseDTO loadStaffs(Long customerId) {
        CustomerEntity customerEntity = customerRepository.findById(customerId)
                .orElseThrow(() -> new InvalidRequestException("Không tìm thầy tòa nhà có ID: " + customerId));
        List<User> staffs = userRepository.findAllByUserRole_CodeAndActiveTrue(SystemConstant.STAFF_ROLE);
        Set<Long> assignmentStaffs = customerEntity.getStaffs()
                .stream().map(User::getId).collect(Collectors.toSet());
        List<StaffResponseDTO> staffResponse = buildStaffResponses(staffs, assignmentStaffs);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(staffResponse);
        return responseDTO;
    }

    @Override
    public ResponseDTO assignmentCustomer(AssignCustomerDTO assignCustomerDTO) {
        CustomerEntity customerEntity = customerRepository.findById(assignCustomerDTO.getCustomerId())
                .orElseThrow(() -> new InvalidRequestException("Không tìm thấy tòa nhà"));
        List<User> staffs = userRepository.findAllById(assignCustomerDTO.getStaffIds());
        if(staffs.size() != assignCustomerDTO.getStaffIds().size()){
            throw new InvalidRequestException("Nhân viên không tồn tại");
        }
        customerEntity.getStaffs().clear();
        customerEntity.getStaffs().addAll(staffs);
        customerRepository.save(customerEntity);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Giao toà nhà thành công");
        return responseDTO;
    }

    @Override
    public ResponseDTO deleteCustomer(List<Long> ids) {
        if (ids == null || ids.isEmpty() || ids.contains(null)) {
            throw new InvalidRequestException("Không có ID khách hàng được cung cấp");
        }
        List<CustomerEntity> customers = customerRepository.findAllById(ids);
        if(customers.size() != ids.size()){
            throw new InvalidRequestException("Id khách hàng không tồn tại");
        }
        customers.forEach(customer -> customer.setActive(false));
        customerRepository.saveAll(customers);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Xóa khách hàng thành công");
        return responseDTO;
    }

    @Override
    public ResponseDTO saveContact(ContactRequest request) {
        String phone = (request.getPhone() != null) ? request.getPhone().trim() : "";
        if(customerRepository.existsByPhoneAndIsActiveTrue(phone)){
            throw new DuplicateResourceException("Số điện thoại đã tồn tại");
        }
        CustomerEntity customer = customerConverter.toCustomerContact(request);
        customer.setStatus(Status.CHUA_XU_LY);
        customer.setActive(true);
        customerRepository.save(customer);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("Gửi liên hệ thành công");
        return responseDTO;
    }

    private List<StaffResponseDTO> buildStaffResponses(List<User> staffs, Set<Long> assignmentStaffs) {
        List<StaffResponseDTO> staffResponseDTOs = new ArrayList<>();
        for(User staff : staffs){
            StaffResponseDTO staffResponseDTO = new StaffResponseDTO();
            staffResponseDTO.setUserName(staff.getUserName());
            staffResponseDTO.setId(staff.getId());
            staffResponseDTO.setChecked(assignmentStaffs.contains(staff.getId()) ? "checked" : "");
            staffResponseDTOs.add(staffResponseDTO);
        }
        return staffResponseDTOs;
    }
}
