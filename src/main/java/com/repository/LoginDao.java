package com.repository;

import com.dto.LoginDaoDTO;
import com.dto.LoginRequest;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class LoginDao {
    public boolean validate(LoginDaoDTO req) {

        String sha = "3b9ca4445ff0e0881f7091fde17c349c5fc8396b62a851d89643c054e4cd5ba0";
        System.out.println(req);

        return Objects.equals(req.getPassword(), sha);
    }
}
