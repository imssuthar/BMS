package com.service;


import com.dto.LoginDaoDTO;
import com.dto.LoginRequest;
import com.dto.LoginResponse;
import com.repository.LoginDao;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class LoginServiceImpl implements LoginI {

    @Autowired
    private LoginDao dao;

    @Override
    public LoginResponse login(LoginRequest req) {
        String email = req.getEmail();
        String password = req.getPassword();
        password = DigestUtils.sha256Hex(password);
        if(dao.validate(new LoginDaoDTO(email,password))) {
            Date time = new Date();
            time.setHours(time.getHours() + 24);
            return LoginResponse.builder().token("rkghegwq").build();
        }
    return null;
    }
}
