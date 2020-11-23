package com.crud.neoclients.api.security.model;

import com.crud.neoclients.api.security.enums.ProfileEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

    private String username;
    private String password;
    private ProfileEnum profile;

    public User(String username, String password, ProfileEnum profile) {
        this.username = username;
        this.password = password;
        this.profile = profile;
    }
}
