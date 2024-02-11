package com.app.service.controllers;

import com.app.service.dtos.GetUserDto;
import com.app.service.dtos.SaveUserDto;
import com.app.service.dtos.UpdateUserDto;
import com.app.service.services.contract.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users/")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("save")
    public ResponseEntity<SaveUserDto> saveUser(@ModelAttribute SaveUserDto savedUserDto, @RequestParam("photo") MultipartFile file) throws IOException {
        String originalPhotoName = file.getOriginalFilename();
        savedUserDto.setPhotoPath(originalPhotoName);

        return new ResponseEntity<>(userService.saveUser(savedUserDto, file), HttpStatus.CREATED);
    }

    @PutMapping("update/{userId}")
    public ResponseEntity<UpdateUserDto> updateUser(@PathVariable("userId") Long userId, @ModelAttribute UpdateUserDto updateUserDto, @RequestParam(value = "photo", required = false) MultipartFile file) throws IOException {

        UpdateUserDto updatedUser = userService.updateUser(userId, updateUserDto, file);

        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("all")
    public ResponseEntity<List<GetUserDto>> getAllClients() {
        List<GetUserDto> getUserDtoList = userService.getAllClients();
        if (getUserDtoList.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(getUserDtoList);
    }

    @GetMapping("{userId}")
    public ResponseEntity<GetUserDto> getClientById(@PathVariable("userId") Long userId) {
        GetUserDto getUserDtoById = userService.getClientById(userId);
        if (getUserDtoById == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(getUserDtoById);
    }

    @DeleteMapping("disable/{userId}")
    public ResponseEntity<String> disableClient(@PathVariable("userId") Long userId) {
        String disableClientMessage = userService.disableClient(userId);

        if (disableClientMessage != null) {
            return new ResponseEntity<>(disableClientMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
