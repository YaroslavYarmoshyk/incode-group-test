package com.incodegroup.test.service.impl;

import com.incodegroup.test.enumeration.SystemErrorCode;
import com.incodegroup.test.exeption.SystemException;
import com.incodegroup.test.model.User;
import com.incodegroup.test.repository.UserRepository;
import com.incodegroup.test.repository.document.UserEntity;
import com.incodegroup.test.rest.dto.UserDto;
import com.incodegroup.test.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Override
    public Set<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> mapper.map(user, UserDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public UserDto getById(final String userId) {
        return userRepository.findById(userId)
                .map(user -> mapper.map(user, UserDto.class))
                .orElseThrow(() -> new SystemException("Cannot find user by id: " + userId, SystemErrorCode.BAD_REQUEST));
    }

    @Override
    public UserDto create(final User user) {
        validateUserCreation(user);
        final UserEntity userEntity = mapper.map(user, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        final UserEntity savedUserEntity = userRepository.save(userEntity);
        return mapper.map(savedUserEntity, UserDto.class);
    }

    private void validateUserCreation(final User user) {
        final String email = user.getEmail();
        final String password = user.getPassword();

        if (Strings.isBlank(email) || Strings.isBlank(password)) {
            throw new SystemException("Email or password cannot be empty", SystemErrorCode.BAD_REQUEST);
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new SystemException("User with email " + email + " already exists", SystemErrorCode.BAD_REQUEST);
        }
    }

    @Override
    public UserDto update(final User user) {
        final String userId = user.getId();
        final UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new SystemException(
                        "Cannot update user with id: " + userId + ". User not found",
                        SystemErrorCode.BAD_REQUEST)
                );
        final String newPassword = Optional.ofNullable(user.getPassword())
                .map(passwordEncoder::encode)
                .orElse(userEntity.getPassword());
        final UserEntity userToUpdate = mapper.map(user, UserEntity.class);
        userToUpdate.setPassword(newPassword);
        final UserEntity savedUserEntity = userRepository.save(userToUpdate);
        return mapper.map(savedUserEntity, UserDto.class);
    }

    @Override
    public UserDto remove(final String userId) {
        final UserDto userToRemove = userRepository.findById(userId)
                .map(user -> mapper.map(user, UserDto.class))
                .orElseThrow(() -> new SystemException(
                        "Cannot delete user by id: " + userId + ". User not found",
                        SystemErrorCode.BAD_REQUEST)
                );
        userRepository.deleteById(userId);
        log.info("User with id: {} was removed", userId);
        return userToRemove;
    }
}
