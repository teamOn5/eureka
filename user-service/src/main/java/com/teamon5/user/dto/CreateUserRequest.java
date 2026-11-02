package com.teamon5.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateUserRequest {
    private String username;
    private String name;
    private String email;

    public CreateUserRequest(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
    }
}
