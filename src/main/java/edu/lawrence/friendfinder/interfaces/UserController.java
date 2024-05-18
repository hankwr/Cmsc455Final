package edu.lawrence.friendfinder.interfaces;

import edu.lawrence.friendfinder.services.JwtService;
import edu.lawrence.friendfinder.services.UserService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.lawrence.friendfinder.exceptions.DuplicateException;
import edu.lawrence.friendfinder.interfaces.dtos.UserDTO;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private UserService us;

    private JwtService jwtService;
    
    public UserController(UserService us, JwtService jwtService) {
        this.us = us;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO user) {
        if (user.getUsername().isBlank() || user.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        }
        String key;
        try {
        key = us.save(user);
        } catch(DuplicateException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
        } 
        String token = jwtService.makeJwt(key);
        user.setToken(token);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}