package org.example.collaborative_cooking.recipe;

import jakarta.persistence.EntityNotFoundException;
import org.example.collaborative_cooking.recipe.dto.RequestRecipe;
import org.example.collaborative_cooking.recipe.dto.ResponseRecipe;
import org.example.collaborative_cooking.recipe.entity.Recipe;
import org.example.collaborative_cooking.recipe.entity.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository repository;

    public RecipeService(RecipeRepository repository) {
        this.repository = repository;
    }

    public ResponseRecipe create(RequestRecipe recipe) {
        Recipe recipeEntity = new Recipe(recipe.getTitle(), recipe.getDescription());
        return ResponseRecipe.toModel(repository.save(recipeEntity));
    }

    public List<ResponseRecipe> findAll() {
        List<Recipe> entities = repository.findAll();
        return entities.stream()
                .map(ResponseRecipe::toModel)
                .collect(Collectors.toList());
    }

    public ResponseRecipe findOne(Long id) {
        Recipe entity = repository.findById(id).orElse(null);
        if (entity == null) throw new EntityNotFoundException("Recipe not found");

        return ResponseRecipe.toModel(entity);
    }

    public ResponseRecipe update(Long id, RequestRecipe updatedRecipe) {
        Recipe existingRecipe = repository.findById(id).orElse(null);
        if (existingRecipe == null) throw new EntityNotFoundException("Recipe not found");

        existingRecipe.setTitle(updatedRecipe.getTitle());
        existingRecipe.setDescription(updatedRecipe.getDescription());
        return ResponseRecipe.toModel(repository.save(existingRecipe));
    }

    public Long deleteById(Long id) {
        repository.deleteById(id);
        return id;
    }
}
