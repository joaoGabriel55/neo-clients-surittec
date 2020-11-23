package com.crud.neoclients.api.security.controller;

import com.crud.neoclients.api.controllers.response.Response;
import com.crud.neoclients.api.security.jwt.JwtAuthenticationRequest;
import com.crud.neoclients.api.security.jwt.JwtTokenUtil;
import com.crud.neoclients.api.security.model.CurrentUser;
import com.crud.neoclients.api.security.model.User;
import com.crud.neoclients.api.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@CrossOrigin(origins = "*")
public class AuthenticationRestController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/auth")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<?> createAuthentication(@RequestBody JwtAuthenticationRequest authenticationRequest)
            throws Exception {

        Response<User> response = new Response<>();

        final Authentication auth = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final User user = userService.getUserByUsername(authenticationRequest.getUsername());


        if (user == null) {
            response.getErrors().add("Username não cadastrado");
            return ResponseEntity.badRequest().body(response);
        }

        if (!authenticationRequest.getPassword().equals(user.getPassword())) {
            response.getErrors().add("Password é incorreto.");
            return ResponseEntity.badRequest().body(response);
        }

        user.setPassword(null);
        return ResponseEntity.ok(new CurrentUser(token, user));

    }

    @PostMapping(value = "/refresh/{token}")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(@PathVariable("token") String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        final User user = null;

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new CurrentUser(refreshedToken, user));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    public Authentication authenticate(String email, String password) throws AuthenticationException {
        return new UsernamePasswordAuthenticationToken(email, password);
    }

}
