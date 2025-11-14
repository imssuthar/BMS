package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginDaoDTO {
    String email;
    String password;
}
