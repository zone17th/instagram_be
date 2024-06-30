package org.app.instagram_be.model.dto;

import lombok.Data;

@Data
public class LoginDTO {
    private String email;
    private String password;
}
