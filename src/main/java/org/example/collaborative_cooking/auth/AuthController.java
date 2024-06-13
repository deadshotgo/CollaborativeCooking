package org.example.collaborative_cooking.auth;

import jakarta.validation.Valid;
import org.example.collaborative_cooking.auth.dto.RequestAuth;
import org.example.collaborative_cooking.auth.dto.ResponseAuth;
import org.example.collaborative_cooking.filter.JwtService;
import org.example.collaborative_cooking.user.UserService;
import org.example.collaborative_cooking.user.dto.RequestUser;
import org.example.collaborative_cooking.user.dto.ResponseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/generate")
    public ResponseEntity<ResponseAuth> authenticateAndGetToken(@Valid @RequestBody RequestAuth requestAuth) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestAuth.getUsername(), requestAuth.getPassword()));
        if (authentication.isAuthenticated()) {
            ResponseAuth response = new ResponseAuth(jwtService.generateToken(requestAuth.getUsername()));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    @PostMapping("/create-user")
    public ResponseEntity<ResponseAuth> registrationUser(@Valid @RequestBody RequestUser requestUser){
        ResponseUser responseUser = userService.createUserHasAuth(requestUser);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestUser.getUsername(), requestUser.getPassword()));
        if (authentication.isAuthenticated()){
            ResponseAuth response = new ResponseAuth(jwtService.generateToken(responseUser.getUsername()), responseUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }
}
