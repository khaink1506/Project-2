package com.devon.building.api.admin;

import com.devon.building.model.dto.AssignCustomerDTO;
import com.devon.building.model.dto.ResponseDTO;
import com.devon.building.model.request.ContactRequest;
import com.devon.building.model.request.CustomerRequest;
import com.devon.building.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerAPI {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ResponseDTO> createCustomer(@RequestBody @Valid CustomerRequest request){
        return ResponseEntity.ok().body(customerService.createCustomer(request));
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateCustomer(@RequestBody @Valid CustomerRequest request){
        return ResponseEntity.ok().body(customerService.updateCustomer(request));
    }

    @GetMapping("/{id}/staff")
    public ResponseEntity<ResponseDTO> loadStaffs(@PathVariable Long id){
        return ResponseEntity.ok(customerService.loadStaffs(id));
    }

    @PutMapping("/assign")
    public ResponseEntity<ResponseDTO> assignCustomer(@RequestBody @Valid AssignCustomerDTO assignCustomerDTO){
        return ResponseEntity.ok().body(customerService.assignmentCustomer(assignCustomerDTO));
    }

    @DeleteMapping("/{ids}")
    public ResponseEntity<ResponseDTO> deleteCustomer(@PathVariable List<Long> ids) {
        return ResponseEntity.ok().body(customerService.deleteCustomer(ids));
    }

    @PostMapping("/contact")
    public ResponseEntity<ResponseDTO> saveContact(@RequestBody @Valid ContactRequest request){
        return ResponseEntity.ok().body(customerService.saveContact(request));
    }

}
