package com.incodegroup.test.rest;

import com.incodegroup.test.model.User;
import com.incodegroup.test.rest.dto.UserDto;
import com.incodegroup.test.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping(value = "/api/users")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @GetMapping
    public Set<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/{user-id}")
    public UserDto getUserById(@PathVariable(value = "user-id") final String userId) {
        return userService.getById(userId);
    }

    @PostMapping
    public UserDto createNewUser(@RequestBody final User user) {
        return userService.create(user);
    }

    @PutMapping
    public UserDto updateUser(@RequestBody final User user) {
        return userService.update(user);
    }

    @DeleteMapping(value = "/{user-id}")
    public UserDto removeUserById(@PathVariable(value = "user-id") final String userId) {
        return userService.remove(userId);
    }
}
