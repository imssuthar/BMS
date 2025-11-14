package com.service;

import com.dto.LoginRequest;
import com.dto.LoginResponse;

public interface LoginI {
    public LoginResponse login(LoginRequest loginRequest);
}
