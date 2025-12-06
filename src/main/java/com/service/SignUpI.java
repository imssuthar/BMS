package com.service;

import com.dto.LoginResponse;
import com.dto.SignUpRequest;

public interface SignUpI {
    LoginResponse signUp(SignUpRequest signUpRequest);
}

