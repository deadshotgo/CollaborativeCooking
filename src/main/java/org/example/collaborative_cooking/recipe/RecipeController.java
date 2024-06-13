package org.example.collaborative_cooking.recipe;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.collaborative_cooking.recipe.dto.RequestRecipe;
import org.example.collaborative_cooking.recipe.dto.ResponseRecipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recipes")
@Tag(name = "Recipe", description = "Endpoints for recipes")
@Validated
@SecurityRequirement(name = "jwt-auth")
public class RecipeController {

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<ResponseRecipe> createRecipe(@Valid @RequestBody RequestRecipe requestRecipe) {
        ResponseRecipe createdRecipe = recipeService.create(requestRecipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<List<ResponseRecipe>> getAllRecipes() {
        List<ResponseRecipe> recipes = recipeService.findAll();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseRecipe> getRecipeById(@PathVariable Long id) {
        ResponseRecipe recipe = recipeService.findOne(id);
        return ResponseEntity.ok(recipe);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseRecipe> updateRecipe(@PathVariable Long id, @Valid @RequestBody RequestRecipe requestRecipe) {
        ResponseRecipe updatedRecipe = recipeService.update(id, requestRecipe);
        return ResponseEntity.ok(updatedRecipe);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteRecipeById(@PathVariable Long id) {
        Long deletedRecipeId = recipeService.deleteById(id);
        return ResponseEntity.ok(deletedRecipeId);
    }
}
