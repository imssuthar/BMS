package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class DeleteAccountRequest {
    // JWT token will be passed in Authorization header, not in body
    // This DTO is kept for consistency, but body can be empty
}

