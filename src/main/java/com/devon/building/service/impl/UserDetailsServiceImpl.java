package com.devon.building.service.impl;

import com.devon.building.entity.User;
import com.devon.building.repository.impl.AccountRepository;
import com.devon.building.repository.UserRepository;
import com.devon.building.security.CustomUserDetails;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserNameAndActiveTrue(username);
        log.info("User {}", user);

        if (user == null) {
            throw new UsernameNotFoundException("User "
                    + username + " was not found in the database");
        }

//        // EMPLOYEE,MANAGER,..
//        String role = user.getUserRole();
//
//        List<GrantedAuthority> grantList = new ArrayList<>();
//
//        // ROLE_EMPLOYEE, ROLE_MANAGER
//        GrantedAuthority authority = new SimpleGrantedAuthority(role);
//
//        grantList.add(authority);
//
//        boolean enabled = user.isActive();
//        boolean accountNonExpired = true;
//        boolean credentialsNonExpired = true;
//        boolean accountNonLocked = true;
//
//        return  new org.springframework.security.core.userdetails.User(user.getUserName(), //
//                user.getEncrytedPassword(), enabled, accountNonExpired, //
//                credentialsNonExpired, accountNonLocked, grantList);
        return new CustomUserDetails(user);
    }
}
