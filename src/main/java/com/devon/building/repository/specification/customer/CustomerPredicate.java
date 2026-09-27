package com.devon.building.repository.specification.customer;


import com.devon.building.builder.BuildingSearchBuilder;
import com.devon.building.builder.CustomerSearchBuilder;
import com.devon.building.entity.BuildingEntity;
import com.devon.building.entity.CustomerEntity;
import com.devon.building.entity.User;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class CustomerPredicate {

    private CustomerPredicate() {

    }
    public static Predicate build(
            Root<CustomerEntity> root,
            CriteriaBuilder cb,
            CustomerSearchBuilder builder){

        List<Predicate> predicates = new ArrayList<>();

        if (builder.getFullName() != null && !builder.getFullName().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("fullName")), "%" + builder.getFullName().trim().toLowerCase() + "%"));
        }
        if (builder.getPhone() != null && !builder.getPhone().trim().isEmpty()) {
            predicates.add(cb.like(root.get("phone"), "%" + builder.getPhone().trim() + "%"));
        }
        if (builder.getEmail() != null && !builder.getEmail().trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("email")), "%" + builder.getEmail().trim().toLowerCase() + "%"));
        }

        if (builder.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), builder.getStatus()));
        }
        if (builder.getStaffId() != null) {
            Join<CustomerEntity, User> staffJoin = root.join("staffs", JoinType.INNER);
            predicates.add(cb.equal(staffJoin.get("id"), builder.getStaffId()));
        }
        // Customer chỉ lấy bản ghi đang active
        predicates.add(cb.isTrue(root.get("isActive")));
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
