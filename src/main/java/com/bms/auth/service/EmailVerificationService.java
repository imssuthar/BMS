package com.bms.auth.service;

import com.bms.auth.dto.LoginResponse;
import com.bms.auth.dto.VerifyEmailRequest;

public interface EmailVerificationService {
    LoginResponse verifyEmail(VerifyEmailRequest request);
}

