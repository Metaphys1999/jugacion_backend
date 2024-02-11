package com.app.service.services.contract;

import com.app.service.dtos.RegisterRequest;
import com.app.service.response.AuthResponse;

public interface AuthService {

    String changePassword(String email);

    AuthResponse register(RegisterRequest request);
}
