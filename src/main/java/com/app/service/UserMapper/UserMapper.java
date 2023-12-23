package com.app.service.UserMapper;

import com.app.service.dtos.UserDto;
import com.app.service.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);
}
