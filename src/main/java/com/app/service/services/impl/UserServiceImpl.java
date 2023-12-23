package com.app.service.services.impl;

import com.app.service.UserMapper.UserMapper;
import com.app.service.dtos.UserDto;
import com.app.service.repository.UserRepository;
import com.app.service.services.contract.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDto saveUser(UserDto savedClientDto) {

        logger.info("Save User");
        return null;
    }
}
