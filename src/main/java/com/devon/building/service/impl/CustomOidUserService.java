package com.devon.building.service.impl;

import com.devon.building.constant.SystemConstant;
import com.devon.building.entity.User;
import com.devon.building.enums.UserRole;
import com.devon.building.repository.UserRepository;
import com.devon.building.utils.OAuth2PictureFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOidUserService extends OidcUserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OAuth2PictureFetcher oAuth2PictureFetcher;

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
                    // Tạo mới user
                    return createGoogleUser(oidcUser);
                });

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getUserRole());
        return new DefaultOidcUser(Collections.singleton(grantedAuthority),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                "email");
    }
    private User createGoogleUser(OidcUser oidcUser){
        return userRepository.save(User.builder()
                        .userName(oidcUser.getEmail())
                        .active(oidcUser.getEmailVerified())
                        .userRole(SystemConstant.USER_ROLE)
                        .fullName(oidcUser.getFullName())
                        .googleAccountId(oidcUser.getAttributes().get("sub").toString())
                        .encryptedPassword(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .image(oAuth2PictureFetcher.fetchGoogleProfilePicture(oidcUser.getAttributes().get("picture").toString()))
                .build());
    }

}
