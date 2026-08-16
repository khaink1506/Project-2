package com.devon.building.repository.specification;

import com.devon.building.builder.BuildingSearchBuilder;
import com.devon.building.entity.BuildingEntity;
import com.devon.building.entity.RentAreaEntity;
import com.devon.building.entity.User;
import jakarta.persistence.criteria.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BuildingPredicate {

    private BuildingPredicate() {

    }
    public static Predicate build(
            Root<BuildingEntity> root,
            CriteriaBuilder cb,
            BuildingSearchBuilder builder){


        List<Predicate> predicates = new ArrayList<>();
        queryNormal(root, cb, builder, predicates);
        querySpecial(root, cb, builder, predicates);
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private static void queryNormal(Root<BuildingEntity> root,
                                    CriteriaBuilder cb,
                                    BuildingSearchBuilder buildingSearchBuilder,
                                    List<Predicate> predicates){
        try {
            Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (!fieldName.equals("staffId") && !fieldName.equals("district") && !fieldName.equals("typeCode") &&
                        !fieldName.startsWith("area") && !fieldName.startsWith("rentPrice")) {
                    Object value= field.get(buildingSearchBuilder);
                    if(value != null && !value.toString().isBlank()){
                        if(value.toString().matches("\\d+(\\.\\d+)?$")){
                            predicates.add(cb.equal(root.get(fieldName), value));
                        }else{
                            predicates.add(cb.like(root.get(fieldName),"%" + value + "%"));
                        }
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void querySpecial(Root<BuildingEntity> root,
                                     CriteriaBuilder cb,
                                     BuildingSearchBuilder builder,
                                        List<Predicate> predicates) {
        if (builder.getDistrict() != null && !builder.getDistrict().isBlank()) {
            predicates.add(cb.equal(root.get("district"), builder.getDistrict()));
        }
        if (builder.getStaffId() != null) {
            Join<BuildingEntity, User> join = root.join("user", JoinType.INNER);
            predicates.add(cb.equal(join.get("id"), builder.getStaffId()));
        }
        if (builder.getAreaFrom() != null || builder.getAreaTo() != null) {
            Join<BuildingEntity, RentAreaEntity> join = root.join("rentArea", JoinType.INNER);
            if (builder.getAreaFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(join.get("value"), builder.getAreaFrom()));
            }

            if (builder.getAreaTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(join.get("value"), builder.getAreaTo()));
            }
        }

        if (builder.getRentPriceFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), builder.getRentPriceFrom()));
        }

        if (builder.getRentPriceTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), builder.getRentPriceTo()));
        }

        if (builder.getTypeCode() != null && !builder.getTypeCode().isEmpty()) {
            List<Predicate> types = new ArrayList<>();
            for (String type : builder.getTypeCode()) {
                types.add(cb.like(root.get("rentType"), "%" + type + "%"));
            }
            predicates.add(cb.or(types.toArray(new Predicate[0])));
        }

    }
}
