package com.devon.building.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "assignmentbuilding")
public class AssignmentBuilding implements Serializable {
    @Serial
    private static final long serialVersionUID = -134783375845L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "createddate", nullable = false)
    Date createDate;

    @Column(name = "modifieddate")
    Date modifiedDate;

    @Column(name = "createdby")
    String createdBy;

    @Column(name = "modifiedby")
    String modifiedBy;

    @ManyToOne
    @JoinColumn(name = "staffid")
    User user;
    @ManyToOne
    @JoinColumn(name = "buildingid")
    BuildingEntity building;
}
