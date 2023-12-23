package com.app.service.services.contract;

import com.app.service.dtos.UserDto;

public interface UserService {

    UserDto saveUser(UserDto savedClientDto);
}
