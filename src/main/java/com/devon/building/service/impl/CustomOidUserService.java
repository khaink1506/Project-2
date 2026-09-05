package com.devon.building.service.impl;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.Role;
import com.devon.building.entity.User;
import com.devon.building.repository.RoleRepository;
import com.devon.building.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOidUserService extends OidcUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        log.info(oidcUser.getClaims().toString());
        String email = oidcUser.getEmail();
        if(email == null || email.isEmpty()){
            throw new OAuth2AuthenticationException("Email address is invalid");
        }
        User user = userRepository.findByUserNameAndActiveTrue(email)
                .orElseGet(() -> {
                    log.info("User not found, create new user");
                    return createGoogleUser(oidcUser);
                });

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getUserRole().getCode());
        return new DefaultOidcUser(Collections.singleton(grantedAuthority),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                "email");
    }
    private User createGoogleUser(OidcUser oidcUser){
        Role role = roleRepository.findByCode(SystemConstant.USER_ROLE)
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
        return userRepository.save(User.builder()
                        .userName(oidcUser.getEmail())
                        .active(oidcUser.getEmailVerified())
                        .userRole(role)
                        .fullName(oidcUser.getFullName())
                        .googleAccountId(oidcUser.getAttributes().get("sub").toString())
                        .encryptedPassword(passwordEncoder.encode(UUID.randomUUID().toString()))
                .build());
    }

}
