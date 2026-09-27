package com.devon.building.repository;

import com.devon.building.entity.CustomerEntity;
import com.devon.building.repository.custom.CustomerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>,
        JpaSpecificationExecutor<CustomerEntity>, CustomerRepositoryCustom {

    boolean existsByPhoneAndIsActiveTrue(String phone);

    boolean existsByPhoneAndIsActiveTrueAndIdNot(String phone, Long id);
}
