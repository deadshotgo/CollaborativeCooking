package org.example.collaborative_cooking;

import jakarta.persistence.EntityNotFoundException;
import org.example.collaborative_cooking.recipe.RecipeService;
import org.example.collaborative_cooking.recipe.dto.RequestRecipe;
import org.example.collaborative_cooking.recipe.dto.ResponseRecipe;
import org.example.collaborative_cooking.recipe.entity.Recipe;
import org.example.collaborative_cooking.recipe.entity.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTests {

    @Mock
    private RecipeRepository repository;

    @InjectMocks
    private RecipeService service;

    @Test
    public void testCreateRecipe() {
        // Arrange
        RequestRecipe requestRecipe = new RequestRecipe("Test Title", "Test Description");
        Recipe savedRecipe = new Recipe(1L, "Test Title", "Test Description");
        when(repository.save(any(Recipe.class))).thenReturn(savedRecipe);

        // Act
        ResponseRecipe response = service.create(requestRecipe);

        // Assert
        assertNotNull(response);
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Description", response.getDescription());
    }

    @Test
    public void testFindAllRecipes() {
        // Arrange
        Recipe recipe1 = new Recipe(1L, "Title1", "Description1");
        Recipe recipe2 = new Recipe(2L, "Title2", "Description2");
        when(repository.findAll()).thenReturn(Arrays.asList(recipe1, recipe2));

        // Act
        List<ResponseRecipe> recipes = service.findAll();

        // Assert
        assertNotNull(recipes);
        assertEquals(2, recipes.size());
        assertEquals("Title1", recipes.get(0).getTitle());
        assertEquals("Title2", recipes.get(1).getTitle());
    }

    @Test
    public void testFindOneRecipe() {
        // Arrange
        Long id = 1L;
        Recipe recipe = new Recipe(id, "Test Title", "Test Description");
        when(repository.findById(id)).thenReturn(Optional.of(recipe));

        // Act
        ResponseRecipe response = service.findOne(id);

        // Assert
        assertNotNull(response);
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Description", response.getDescription());
    }

    @Test
    public void testFindOneRecipe_NotFound() {
        // Arrange
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.findOne(id));
    }

    @Test
    public void testUpdateRecipe() {
        // Arrange
        Long id = 1L;
        RequestRecipe updatedRecipe = new RequestRecipe("Updated Title", "Updated Description");
        Recipe existingRecipe = new Recipe(id, "Test Title", "Test Description");
        when(repository.findById(id)).thenReturn(Optional.of(existingRecipe));
        when(repository.save(any(Recipe.class))).thenReturn(new Recipe(id, "Updated Title", "Updated Description"));

        // Act
        ResponseRecipe response = service.update(id, updatedRecipe);

        // Assert
        assertNotNull(response);
        assertEquals("Updated Title", response.getTitle());
        assertEquals("Updated Description", response.getDescription());
    }

    @Test
    public void testUpdateRecipe_NotFound() {
        // Arrange
        Long id = 999L;
        RequestRecipe updatedRecipe = new RequestRecipe("Updated Title", "Updated Description");
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.update(id, updatedRecipe));
    }

    @Test
    public void testDeleteRecipe() {
        // Arrange
        Long id = 1L;
        doNothing().when(repository).deleteById(id);

        // Act
        Long deletedId = service.deleteById(id);

        // Assert
        assertEquals(id, deletedId);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteRecipe_NotFound() {
        // Arrange
        Long id = 999L;
        doThrow(EntityNotFoundException.class).when(repository).deleteById(id);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> service.deleteById(id));
    }
}
