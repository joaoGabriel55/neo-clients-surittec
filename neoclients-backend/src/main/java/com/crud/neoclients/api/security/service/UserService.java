package com.crud.neoclients.api.security.service;

import com.crud.neoclients.api.security.enums.ProfileEnum;
import com.crud.neoclients.api.security.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    public User getUserByUsername(String username) {
        if (username.equals("admin")) {
            return new User("admin", "123456", ProfileEnum.ROLE_ADMIN);
        } else if (username.equals("comum")) {
            return new User("comum", "123456", null);
        }
        return null;
    }


}
