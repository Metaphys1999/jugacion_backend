package com.app.service.services.impl;

import com.app.service.dtos.GetUserDto;
import com.app.service.dtos.SaveUserDto;
import com.app.service.dtos.UpdateUserDto;
import com.app.service.enums.UserStatus;
import com.app.service.mapper.UserMapper;
import com.app.service.entities.User;
import com.app.service.repository.UserRepository;
import com.app.service.services.contract.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public static String uploadDirectory =
            System.getProperty("user.dir") + "/src/main/webapp/images/users";

    @Override
    public SaveUserDto saveUser(SaveUserDto savedSaveUserDto, MultipartFile file) throws IOException {

        logger.info("Save User");

        String originalPhotoName = file.getOriginalFilename();
        Path photoNameAndPath = Paths.get(uploadDirectory, originalPhotoName);
        Files.write(photoNameAndPath, file.getBytes());

        savedSaveUserDto.setPhotoPath(originalPhotoName);

        User newUser = userMapper.userDtoToUser(savedSaveUserDto);

        newUser.setPhotoPath(savedSaveUserDto.getPhotoPath());

        newUser = userRepository.save(newUser);

        SaveUserDto newSaveUserDto = userMapper.userToUserDto(newUser);

        return newSaveUserDto;
    }

    @Override
    public UpdateUserDto updateUser(Long userId, UpdateUserDto updatedSaveUserDto, MultipartFile file) throws IOException {
        logger.info("Update User");

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The User with ID: " + userId + " was not found."));

        existingUser.setName(updatedSaveUserDto.getName());
        existingUser.setLastName(updatedSaveUserDto.getLastName());
        existingUser.setEmail(updatedSaveUserDto.getEmail());
        existingUser.setPhone(updatedSaveUserDto.getPhone());
        existingUser.setAddress(updatedSaveUserDto.getAddress());

        if (file != null && !file.isEmpty()) {
            String originalPhotoName = file.getOriginalFilename();
            Path photoNameAndPath = Paths.get(uploadDirectory, originalPhotoName);
            Files.write(photoNameAndPath, file.getBytes());
            existingUser.setPhotoPath(originalPhotoName);
        }

        User updatedUser = userRepository.save(existingUser);


        UpdateUserDto updatedSaveUserDtoResult = userMapper.userToUpdateUserDto(updatedUser);
        return updatedSaveUserDtoResult;
    }

    @Override
    public SaveUserDto deleteClient(Long userId) {
        return null;
    }

    @Override
    public String disableClient(Long userId) {
        logger.info("Disable Client");

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The Client with ID: " + userId + " was not found."));

        if (existingUser.getUserStatus() == UserStatus.ACTIVE) {
            existingUser.setUserStatus(UserStatus.INACTIVE);
        } else {
            existingUser.setUserStatus(UserStatus.ACTIVE);
        }

        User userEntity = userRepository.save(existingUser);

        return "User with ID: " + userId + " is now " +
                (userEntity.getUserStatus() == UserStatus.ACTIVE ? "enabled" : "disabled.");
    }

    @Override
    public List<GetUserDto> getAllClients() {
        logger.info("Get All Clients");
        List<User> clientList = userRepository.findAll();
        List<GetUserDto> clientDtoList = userMapper.userListToGetUserDtoList(clientList);
        return clientDtoList;
    }

    @Override
    public GetUserDto getClientById(Long userId) {
        logger.info("Get Client By Id");

        User clientById = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The Client with ID: " + userId + " was not found."));

        GetUserDto clientDto = userMapper.userToGetUserDto(clientById);
        return clientDto;
    }
}
