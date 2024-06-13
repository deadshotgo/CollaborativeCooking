package org.example.collaborative_cooking.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.collaborative_cooking.user.dto.RequestUser;
import org.example.collaborative_cooking.user.dto.ResponseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "Endpoints for user")
@Validated
@SecurityRequirement(name = "jwt-auth")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(@Qualifier("userService") UserService userService) {
        this.service = userService;
    }


    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponseUser> create(@Valid @RequestBody RequestUser user) {
        ResponseUser createdResponseUser = service.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdResponseUser);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ResponseUser>> findAll() {
        List<ResponseUser> users = service.findAll();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseUser> findOne(@PathVariable Long id) {
        return  ResponseEntity.ok(service.findOne(id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseUser> update(@PathVariable Long id, @Valid @RequestBody RequestUser user) {
        ResponseUser updatedResponseUser = service.update(id, user);
        return ResponseEntity.ok(updatedResponseUser);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }

}
