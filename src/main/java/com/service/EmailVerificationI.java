package com.service;

import com.dto.LoginResponse;
import com.dto.VerifyEmailRequest;

public interface EmailVerificationI {
    LoginResponse verifyEmail(VerifyEmailRequest request);
}

