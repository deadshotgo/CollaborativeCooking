package org.example.collaborative_cooking.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.collaborative_cooking.user.dto.ResponseUser;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseAuth {
    private String token;
    private ResponseUser responseUser;

    public ResponseAuth(String token) {
        this.token = token;
    }

    public ResponseAuth(String token, ResponseUser user) {
        this.token = token;
        this.responseUser = user;
    }
}