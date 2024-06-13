package org.example.collaborative_cooking.event;

import jakarta.persistence.EntityNotFoundException;
import org.example.collaborative_cooking.event.dto.RequestEvent;
import org.example.collaborative_cooking.event.dto.ResponseEvent;
import org.example.collaborative_cooking.event.entity.Event;
import org.example.collaborative_cooking.event.entity.EventRepository;
import org.example.collaborative_cooking.recipe.entity.Recipe;
import org.example.collaborative_cooking.recipe.entity.RecipeRepository;
import org.example.collaborative_cooking.user.entity.User;
import org.example.collaborative_cooking.user.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    @Autowired
    public EventService(EventRepository eventRepository, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    public ResponseEvent create(RequestEvent requestEvent) {
        User user = userRepository.findById(requestEvent.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + requestEvent.getUserId()));

        Recipe recipe = recipeRepository.findById(requestEvent.getRecipeId())
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with id: " + requestEvent.getRecipeId()));

        Event event = new Event();
        event.setName(requestEvent.getName());
        event.setDate(requestEvent.getDate());
        event.setUser(user);
        event.setRecipe(recipe);

        Event savedEvent = eventRepository.save(event);
        return ResponseEvent.toModel(savedEvent);
    }

    public List<ResponseEvent> findAll() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(ResponseEvent::toModel)
                .collect(Collectors.toList());
    }

    public ResponseEvent findById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));
        return ResponseEvent.toModel(event);
    }

    public ResponseEvent update(Long id, RequestEvent requestEvent) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));

        User user = userRepository.findById(requestEvent.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + requestEvent.getUserId()));

        Recipe recipe = recipeRepository.findById(requestEvent.getRecipeId())
                .orElseThrow(() -> new EntityNotFoundException("Recipe not found with id: " + requestEvent.getRecipeId()));

        event.setName(requestEvent.getName());
        event.setDate(requestEvent.getDate());
        event.setUser(user);
        event.setRecipe(recipe);

        Event savedEvent = eventRepository.save(event);
        return ResponseEvent.toModel(savedEvent);
    }

    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }
}
