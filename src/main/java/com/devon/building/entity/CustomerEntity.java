package com.devon.building.entity;

import com.devon.building.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customer")
public class CustomerEntity extends BaseEntity{

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "companyname")
    private String companyName;

    @Column(name = "demand")
    private String demand;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToMany
    @JoinTable(
            name = "assignmentcustomer",
            joinColumns = @JoinColumn(name = "customerid"),
            inverseJoinColumns = @JoinColumn(name = "staffid")
    )
    private Set<User> staffs = new HashSet<>();

    @OneToMany(mappedBy = "customer")
    private List<TransactionEntity> transactions = new ArrayList<>();
}
