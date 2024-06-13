package org.example.collaborative_cooking.recipe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestRecipe {
    @NotBlank
    private String title;

    @NotBlank
    private String description;
}
