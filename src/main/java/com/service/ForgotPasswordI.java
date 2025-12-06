package com.service;

import com.dto.ForgotPasswordRequest;
import com.dto.LoginResponse;
import com.dto.ResetPasswordRequest;
import com.dto.VerifyCodeRequest;

public interface ForgotPasswordI {
    LoginResponse requestPasswordReset(ForgotPasswordRequest request);
    LoginResponse verifyCode(VerifyCodeRequest request);
    LoginResponse resetPassword(ResetPasswordRequest request);
}

