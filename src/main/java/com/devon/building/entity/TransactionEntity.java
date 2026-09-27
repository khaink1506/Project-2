package com.devon.building.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "transaction")
public class TransactionEntity extends BaseEntity{

    @Column(name = "code")
    private String code;

    @Column(name ="note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "customerid", nullable = false)
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "staffid", nullable = false)
    private User staff;

    @Column(name = "is_active")
    private boolean isActive;
}
