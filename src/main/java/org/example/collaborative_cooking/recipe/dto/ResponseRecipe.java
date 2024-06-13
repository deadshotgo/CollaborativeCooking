package org.example.collaborative_cooking.recipe.dto;

import lombok.Data;
import org.example.collaborative_cooking.recipe.entity.Recipe;

@Data
public class ResponseRecipe {
    private String title;
    private String description;

    public static ResponseRecipe toModel(Recipe entity) {
        ResponseRecipe model = new ResponseRecipe();
        model.setTitle(entity.getTitle());
        model.setDescription(entity.getDescription());
        return model;
    }
}
