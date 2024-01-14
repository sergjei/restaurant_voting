package com.github.sergjei.restaurant_voting.controller;

import com.github.sergjei.restaurant_voting.model.AuthUser;
import com.github.sergjei.restaurant_voting.model.Role;
import com.github.sergjei.restaurant_voting.model.User;
import com.github.sergjei.restaurant_voting.repository.UserRepository;
import com.github.sergjei.restaurant_voting.utils.ValidationUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping(value = ProfileController.CURRENT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "basicAuth")
@Transactional
public class ProfileController {

    public static final String CURRENT_URL = "/rest/profile";
    public UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        return authUser.getUser();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        userRepository.deleteById(authUser.id());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser,
                       @Valid @RequestBody User updated) {
        User oldUser = authUser.getUser();
        ValidationUtil.assureIdConsistent(updated, oldUser.id());
        if (updated.getPassword() == null) {
            updated.setPassword(oldUser.getPassword());
        }
        updated.setRoles(Set.of(Role.USER));
        userRepository.save(updated);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
        ValidationUtil.checkNew(user);
        user.setRoles(Set.of(Role.USER));
        User created = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL + "/register/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
