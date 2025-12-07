package com.bms.auth.service;

import com.bms.auth.dto.LoginResponse;

public interface DeleteAccountService {
    LoginResponse deleteAccount(String token);
}

