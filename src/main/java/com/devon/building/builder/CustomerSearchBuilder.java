package com.devon.building.builder;

import com.devon.building.enums.Status;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerSearchBuilder {
    final String fullName;
    final String phone;
    final String email;
    final Long staffId;
    final Status status;

}
