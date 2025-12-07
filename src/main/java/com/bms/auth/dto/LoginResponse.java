package com.bms.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    int statusCode;
    Object data;
    String respText;
}

