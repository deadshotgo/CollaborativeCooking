package org.example.collaborative_cooking;

import jakarta.persistence.EntityNotFoundException;
import org.example.collaborative_cooking.event.EventService;
import org.example.collaborative_cooking.event.dto.RequestEvent;
import org.example.collaborative_cooking.event.dto.ResponseEvent;
import org.example.collaborative_cooking.event.entity.Event;
import org.example.collaborative_cooking.event.entity.EventRepository;
import org.example.collaborative_cooking.recipe.entity.Recipe;
import org.example.collaborative_cooking.recipe.entity.RecipeRepository;
import org.example.collaborative_cooking.user.entity.User;
import org.example.collaborative_cooking.user.entity.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.util.DateUtil.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    public void testCreateEvent() {
        // Arrange
        RequestEvent requestEvent = new RequestEvent("Event Name", now(), 1L, 1L);
        User user = new User(1L, "username", "password", "user@example.com", "ADMIN");
        Recipe recipe = new Recipe(1L, "Recipe Title", "Recipe Description");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEvent response = eventService.create(requestEvent);

        // Assert
        assertNotNull(response);
        assertEquals("Event Name", response.getName());
    }

    @Test
    public void testCreateEvent_UserNotFound() {
        // Arrange
        RequestEvent requestEvent = new RequestEvent("Event Name", now(), 999L, 1L);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> eventService.create(requestEvent));
    }

    @Test
    public void testCreateEvent_RecipeNotFound() {
        // Arrange
        RequestEvent requestEvent = new RequestEvent("Event Name", now(), 1L, 999L);
        User user = new User(1L, "username", "password", "user@example.com", "ADMIN");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(recipeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> eventService.create(requestEvent));
    }

    @Test
    public void testFindAllEvents() {
        // Arrange
        Event event1 = new Event(1L, "Event 1", now(), new User(), new Recipe());
        Event event2 = new Event(2L, "Event 2", now(), new User(), new Recipe());
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event1, event2));

        // Act
        List<ResponseEvent> events = eventService.findAll();

        // Assert
        assertNotNull(events);
        assertEquals(2, events.size());
        assertEquals("Event 1", events.get(0).getName());
        assertEquals("Event 2", events.get(1).getName());
    }

    @Test
    public void testFindEventById() {
        // Arrange
        Long eventId = 1L;
        Event event = new Event(eventId, "Event Name", now(), new User(), new Recipe());
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // Act
        ResponseEvent response = eventService.findById(eventId);

        // Assert
        assertNotNull(response);
        assertEquals("Event Name", response.getName());
    }

    @Test
    public void testFindEventById_NotFound() {
        // Arrange
        Long eventId = 999L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> eventService.findById(eventId));
    }

    @Test
    public void testUpdateEvent() {
        // Arrange
        Long eventId = 1L;
        RequestEvent requestEvent = new RequestEvent("Updated Event Name", now(), 2L, 2L);
        Event existingEvent = new Event(eventId, "Event Name", now(), new User(), new Recipe());
        User user = new User(2L, "updatedusername", "updatedpassword", "updateduser@example.com", "ADMIN");
        Recipe recipe = new Recipe(2L, "Updated Recipe Title", "Updated Recipe Description");
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(recipeRepository.findById(2L)).thenReturn(Optional.of(recipe));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ResponseEvent response = eventService.update(eventId, requestEvent);

        // Assert
        assertNotNull(response);
        assertEquals("Updated Event Name", response.getName());
    }

    @Test
    public void testUpdateEvent_EventNotFound() {
        // Arrange
        Long eventId = 999L;
        RequestEvent requestEvent = new RequestEvent("Updated Event Name", now(), 2L, 2L);
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> eventService.update(eventId, requestEvent));
    }

    @Test
    public void testUpdateEvent_UserNotFound() {
        // Arrange
        Long eventId = 1L;
        RequestEvent requestEvent = new RequestEvent("Updated Event Name", now(), 999L, 2L);
        Event existingEvent = new Event(eventId, "Event Name", now(), new User(), new Recipe());
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> eventService.update(eventId, requestEvent));
    }

    @Test
    public void testUpdateEvent_RecipeNotFound() {
        // Arrange
        Long eventId = 1L;
        RequestEvent requestEvent = new RequestEvent("Updated Event Name", now(), 2L, 999L);
        Event existingEvent = new Event(eventId, "Event Name", now(), new User(), new Recipe());
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(userRepository.findById(2L)).thenReturn(Optional.of(new User()));
        when(recipeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> eventService.update(eventId, requestEvent));
    }

    @Test
    public void testDeleteEvent() {
        // Arrange
        Long eventId = 1L;
        doNothing().when(eventRepository).deleteById(eventId);

        // Act
        eventService.deleteById(eventId);

        // Assert
        verify(eventRepository, times(1)).deleteById(eventId);
    }
}
