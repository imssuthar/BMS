package com.service;

import com.dto.LoginResponse;

public interface DeleteAccountI {
    LoginResponse deleteAccount(String token);
}

