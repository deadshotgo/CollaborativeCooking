package org.example.collaborative_cooking.event;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.collaborative_cooking.event.dto.RequestEvent;
import org.example.collaborative_cooking.event.dto.ResponseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@Tag(name = "Events", description = "Endpoints for events")
@Validated
@SecurityRequirement(name = "jwt-auth")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseEvent> createEvent(@Valid @RequestBody RequestEvent requestEvent) {
        ResponseEvent createdEvent = eventService.create(requestEvent);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ResponseEvent>> getAllEvents() {
        List<ResponseEvent> events = eventService.findAll();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseEvent> getEventById(@PathVariable Long id) {
        ResponseEvent event = eventService.findById(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseEvent> updateEvent(@PathVariable Long id, @Valid @RequestBody RequestEvent requestEvent) {
        ResponseEvent updatedEvent = eventService.update(id, requestEvent);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
