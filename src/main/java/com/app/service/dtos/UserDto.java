package com.app.service.dtos;

import com.app.service.enums.UserRole;
import com.app.service.enums.UserStatus;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.sql.Date;

@Data
public class UserDto {

    private Long clientId;
    private String numberId;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private UserStatus userStatus;
    private UserRole userRole;
    private Date dob;
    @Nullable
    private String photoPath;
}
