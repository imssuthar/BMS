package com.bms.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteAccountRequest {
    // JWT token will be passed in Authorization header, not in body
    // This DTO is kept for consistency, but body can be empty
}

