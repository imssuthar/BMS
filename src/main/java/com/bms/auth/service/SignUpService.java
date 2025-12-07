package com.bms.auth.service;

import com.bms.auth.dto.LoginResponse;
import com.bms.auth.dto.SignUpRequest;

public interface SignUpService {
    LoginResponse signUp(SignUpRequest signUpRequest);
}

