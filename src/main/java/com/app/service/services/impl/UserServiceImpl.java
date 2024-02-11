package com.app.service.services.impl;

import com.app.service.dtos.GetUserDto;
import com.app.service.dtos.SaveUserDto;
import com.app.service.dtos.UpdateUserDto;
import com.app.service.enums.UserStatus;
import com.app.service.mapper.UserMapper;
import com.app.service.entities.User;
import com.app.service.repository.UserRepository;
import com.app.service.services.contract.UserService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("lajugacionapp-80ae3.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        InputStream inputStream = UserServiceImpl.class.getClassLoader().getResourceAsStream("firebase-private-key.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/lajugacionapp-80ae3.appspot.com/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @Override
    public SaveUserDto saveUser(SaveUserDto savedUserDto, MultipartFile multipartFile) throws IOException {

        logger.info("Save User");

        String fileName = multipartFile.getOriginalFilename();
        fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));

        File file = this.convertToFile(multipartFile, fileName);
        String URL = this.uploadFile(file, fileName);
        file.delete();

        savedUserDto.setPhotoPath(URL);
        savedUserDto.setPhotoId(fileName);

        User newUser = userMapper.userDtoToUser(savedUserDto);

        newUser.setPhotoPath(savedUserDto.getPhotoPath());

        newUser = userRepository.save(newUser);

        SaveUserDto newSaveUserDto = userMapper.userToUserDto(newUser);

        return newSaveUserDto;
    }

    @Override
    public UpdateUserDto updateUser(Long userId, UpdateUserDto updateUserDto, MultipartFile multipartFile) throws IOException {
        logger.info("Update User");

        String fileName = multipartFile.getOriginalFilename();
        fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("The User with ID: " + userId + " was not found."));

        String oldPhotoId = existingUser.getPhotoId();

        deleteFileFromFirebase(oldPhotoId);

        File file = this.convertToFile(multipartFile, fileName);
        String URL = this.uploadFile(file, fileName);
        file.delete();

        existingUser.setName(updateUserDto.getName());
        existingUser.setLastName(updateUserDto.getLastName());
        existingUser.setEmail(updateUserDto.getEmail());
        existingUser.setPhone(updateUserDto.getPhone());
        existingUser.setAddress(updateUserDto.getAddress());
        existingUser.setPhotoPath(URL);
        existingUser.setPhotoId(fileName);

        User updatedUser = userRepository.save(existingUser);


        UpdateUserDto updatedUserDto = userMapper.userToUpdateUserDto(updatedUser);
        return updatedUserDto;
    }

    public void deleteFileFromFirebase(String photoId) {
        try {
            BlobId blobId = BlobId.of("lajugacionapp-80ae3.appspot.com", photoId);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
            InputStream inputStream = UserServiceImpl.class.getClassLoader().getResourceAsStream("firebase-private-key.json");
            Credentials credentials = GoogleCredentials.fromStream(inputStream);
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            boolean deleted = storage.delete(blobId);
            if (deleted) {
                System.out.println("Archivo eliminado exitosamente: " + photoId);
            } else {
                System.out.println("No se pudo eliminar el archivo: " + photoId);
            }
        } catch (StorageException | IOException e) {
            System.err.println("Error al eliminar el archivo: " + e.getMessage());
        }
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
