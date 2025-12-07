package com.bms.auth.service;

import com.bms.auth.dto.ForgotPasswordRequest;
import com.bms.auth.dto.LoginResponse;
import com.bms.auth.dto.ResetPasswordRequest;
import com.bms.auth.dto.VerifyCodeRequest;

public interface ForgotPasswordService {
    LoginResponse requestPasswordReset(ForgotPasswordRequest request);
    LoginResponse verifyCode(VerifyCodeRequest request);
    LoginResponse resetPassword(ResetPasswordRequest request);
}

