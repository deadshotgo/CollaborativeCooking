package org.example.collaborative_cooking.user.dto;

import lombok.Data;
import org.example.collaborative_cooking.user.entity.User;

@Data
public class ResponseUser {
    private Long id;
    private String username;
    private String email;
    private String role;

    public static ResponseUser toModel(User entity) {
        ResponseUser model = new ResponseUser();
        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        model.setEmail(entity.getEmail());
        model.setRole(entity.getRole());
        return model;
    }
}
