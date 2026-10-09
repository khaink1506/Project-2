package com.devon.building.repository;

import com.devon.building.entity.User;
import com.devon.building.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);

    Optional<User> findByUserNameAndActiveTrue(String username);

    List<User> findAllByUserRoleAndActiveTrue(String userRole);

    boolean existsByUserName(String username);

}
