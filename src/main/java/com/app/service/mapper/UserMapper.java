package com.app.service.mapper;

import com.app.service.dtos.GetUserDto;
import com.app.service.dtos.SaveUserDto;
import com.app.service.dtos.UpdateUserDto;
import com.app.service.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    SaveUserDto userToUserDto(User user);

    User userDtoToUser(SaveUserDto saveUserDto);

    UpdateUserDto userToUpdateUserDto(User User);

    GetUserDto userToGetUserDto(User User);

    List<GetUserDto> userListToGetUserDtoList(List<User> userList);

    List<User> getUserDtoListToUserList(List<GetUserDto> getUserDtoList);
}
