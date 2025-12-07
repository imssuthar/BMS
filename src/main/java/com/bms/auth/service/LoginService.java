package com.bms.auth.service;

import com.bms.auth.dto.LoginRequest;
import com.bms.auth.dto.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest loginRequest);
}

