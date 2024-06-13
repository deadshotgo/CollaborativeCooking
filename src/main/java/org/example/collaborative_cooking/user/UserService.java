package org.example.collaborative_cooking.user;

import jakarta.persistence.EntityNotFoundException;
import org.example.collaborative_cooking.user.dto.RequestUser;
import org.example.collaborative_cooking.user.dto.ResponseUser;
import org.example.collaborative_cooking.user.entity.User;
import org.example.collaborative_cooking.user.entity.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository repository;


    public ResponseUser create(RequestUser user) {
        User userEntity = new User(user.getUsername(), user.getPassword(), user.getEmail(), user.getRole());
        return ResponseUser.toModel(repository.save(userEntity));
    }

    public List<ResponseUser> findAll() {
        List<User> entities = repository.findAll();
        return entities.stream()
                .map(ResponseUser::toModel)
                .collect(Collectors.toList());
    }

    public ResponseUser findOne(Long id) {
        User entity = repository.findById(id).orElse(null);
        if (entity == null) throw new EntityNotFoundException("User not found");

        return ResponseUser.toModel(entity);
    }

    public ResponseUser update(Long id, RequestUser updatedUser) {
        User existingUser = repository.findById(id).orElse(null);
        if (existingUser == null) throw new EntityNotFoundException("User not found");

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        return ResponseUser.toModel(repository.save(existingUser));
    }

    public Long deleteById(Long id) {
        repository.deleteById(id);
        return id;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userDetail = repository.findByUsername(username);
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new EntityNotFoundException("User not found " + username));
    }

    public ResponseUser createUserHasAuth(RequestUser user) {
        User userEntity = new User();
        userEntity.setUsername(user.getUsername());
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(encoder.encode(user.getPassword()));
        userEntity.setRole("USER");
        return ResponseUser.toModel(repository.save(userEntity));
    }
}
