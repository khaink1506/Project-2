package com.devon.building.entity;

import com.devon.building.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "User")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity implements Serializable{

    @Serial
    private static final long serialVersionUID = -2054386655979281969L;

    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_EMPLOYEE = "STAFF";
    public static final String ROLE_USER = "USER";


    @Column(name = "username", length = 20, nullable = false)
    private String userName;

    @Column(name = "password", length = 128, nullable = false)
    private String encryptedPassword;

    @Column(name = "Active", length = 1, nullable = false)
    private boolean active;

    @Column(name = "userrole", length = 20, nullable = false)
    private String userRole;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "fullname", length = 250, nullable = false)
    private String fullName;

    @Column(name = "phone", length = 10, nullable = false)
    private String phone;

    @Lob
    @Column(name = "image", length = Integer.MAX_VALUE, nullable = true)
    private byte[] image;

    @ManyToMany(mappedBy = "user")
    private Set<BuildingEntity> building = new HashSet<>();

    @ManyToMany(mappedBy = "staffs")
    private Set<CustomerEntity> customers = new HashSet<>();

    @Column(name = "facebook_account_id")
    String facebookAccountId;

    @Column(name = "google_account_id")
    String googleAccountId;

    public User(Long id, String userName, Boolean active, String userRole, String fullName, String phone) {
        this.id = id;
        this.userName = userName;
        this.active = active;
        this.fullName = fullName;
        this.userRole = userRole;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "[" + this.userName + "," + this.encryptedPassword + "," + this.userRole + "]";
    }

}