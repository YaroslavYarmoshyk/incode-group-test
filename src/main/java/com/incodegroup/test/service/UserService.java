package com.incodegroup.test.service;

import com.incodegroup.test.model.User;
import com.incodegroup.test.rest.dto.UserDto;

import java.util.Set;

public interface UserService {

    Set<UserDto> getAllUsers();

    UserDto getById(final String userId);

    UserDto create(final User user);

    UserDto update(final User user);

    UserDto remove(final String userId);
}
