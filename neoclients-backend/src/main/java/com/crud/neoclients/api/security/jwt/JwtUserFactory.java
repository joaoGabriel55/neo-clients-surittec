package com.crud.neoclients.api.security.jwt;

import com.crud.neoclients.api.security.enums.ProfileEnum;
import com.crud.neoclients.api.security.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(user.getUsername(), user.getPassword(), mapToGrantedAuthorities(user.getProfile()));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(ProfileEnum profile) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(profile == null ? "ROLE_COMUM" : profile.toString()));
        return authorities;
    }

}
