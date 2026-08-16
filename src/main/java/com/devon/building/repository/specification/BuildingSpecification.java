package com.devon.building.repository.specification;

import com.devon.building.builder.BuildingSearchBuilder;
import com.devon.building.entity.BuildingEntity;
import org.springframework.data.jpa.domain.Specification;

public final class BuildingSpecification {

    private BuildingSpecification() {
    }

    public static Specification<BuildingEntity> filter(BuildingSearchBuilder builder) {
        return (root, query, cb) ->
        {
            if (query != null) {
                query.distinct(true);
            }
            return BuildingPredicate.build(root, cb, builder);
        };
    }
}
