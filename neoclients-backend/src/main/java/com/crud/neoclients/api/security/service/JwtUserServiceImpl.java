package com.crud.neoclients.api.security.service;

import com.crud.neoclients.api.security.jwt.JwtUserFactory;
import com.crud.neoclients.api.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JwtUserServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException(String.format("Nenhum usuario com o username '%s' foi encontrado.", username));
        else
            return JwtUserFactory.create(user);
    }


}
