package com.app.service.services.contract;

import com.app.service.dtos.GetUserDto;
import com.app.service.dtos.SaveUserDto;
import com.app.service.dtos.UpdateUserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    SaveUserDto saveUser(SaveUserDto savedUserDto, MultipartFile file) throws IOException;

    UpdateUserDto updateUser(Long userId, UpdateUserDto updatedUserDto, MultipartFile file) throws IOException;

    SaveUserDto deleteClient(Long userId);

    String disableClient(Long userId);

    List<GetUserDto> getAllClients();

    GetUserDto getClientById(Long userId);
}
