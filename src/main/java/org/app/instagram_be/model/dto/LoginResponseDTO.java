package org.app.instagram_be.model.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String email;
    private String phoneNumber;
    private String accessToken;
}
