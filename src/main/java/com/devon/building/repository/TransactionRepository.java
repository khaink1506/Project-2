package com.devon.building.repository;

import com.devon.building.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByCustomer_IdAndCodeAndIsActiveTrueOrderByCreatedDateDesc(Long customerId, String code);
}
