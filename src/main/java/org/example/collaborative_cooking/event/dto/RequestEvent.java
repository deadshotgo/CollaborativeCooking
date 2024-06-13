package org.example.collaborative_cooking.event.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestEvent {
    @NotBlank
    private String name;

    @NotNull
    private Date date;

    @NotNull
    public Long userId;

    @NotNull
    public Long recipeId;
}
