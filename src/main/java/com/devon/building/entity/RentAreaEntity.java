package com.devon.building.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Entity
@Table(name="rentarea")
public class RentAreaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "value")
    Long value;

    @ManyToOne
    @JoinColumn(name = "buildingid", nullable = false)
    private BuildingEntity building;

    @Column(name = "createddate")
    Date createdDate;

    @Column(name = "modifieddate")
    Date modifiedDate;

    @Column(name = "createdby")
    String createdBy;

    @Column(name = "modifiedby")
    String modifiedBy;
}

