package com.devon.building.repository.specification.customer;

import com.devon.building.builder.BuildingSearchBuilder;
import com.devon.building.builder.CustomerSearchBuilder;
import com.devon.building.entity.BuildingEntity;
import com.devon.building.entity.CustomerEntity;
import com.devon.building.repository.specification.building.BuildingPredicate;
import org.springframework.data.jpa.domain.Specification;

public final class CustomerSpecification {
    private CustomerSpecification(){

    }
    public static Specification<CustomerEntity> filter(CustomerSearchBuilder builder) {
        return (root, query, cb) ->
        {
            if (query != null) {
                query.distinct(true);
            }
            return CustomerPredicate.build(root, cb, builder);
        };
    }
}
