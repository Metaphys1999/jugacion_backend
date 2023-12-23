package com.app.service.controllers;

import com.app.service.dtos.UserDto;
import com.app.service.services.contract.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/")
public class UserController {

    @Autowired
    private UserService userService;

    public ResponseEntity<UserDto> saveClient(){
        return null;
    }
}
