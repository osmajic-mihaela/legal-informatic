package org.example.legalinformaticbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.legalinformaticbackend.dto.ApplicationUserDTO;
import org.example.legalinformaticbackend.dto.LoginDTO;
import org.example.legalinformaticbackend.model.ApplicationUser;
import org.example.legalinformaticbackend.service.ApplicationUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class ApplicationUserController {

    private final ApplicationUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ApplicationUserDTO newUser) {
        ApplicationUser createdUser = userService.register(newUser);
        if (createdUser != null) return new ResponseEntity(createdUser.getFullName(), HttpStatus.OK);
        else return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto) {
        ApplicationUser user = userService.login(loginDto);
        if (user != null) return new ResponseEntity(user, HttpStatus.OK);
        else return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}
