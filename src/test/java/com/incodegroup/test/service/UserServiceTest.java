package com.incodegroup.test.service;

import com.incodegroup.test.exeption.SystemException;
import com.incodegroup.test.model.User;
import com.incodegroup.test.repository.UserRepository;
import com.incodegroup.test.repository.document.UserEntity;
import com.incodegroup.test.rest.dto.UserDto;
import com.incodegroup.test.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper = new ModelMapper();
    private UserService userService;

    private final static String DEFAULT_USER_ID = "649348c9bb81557c80f11472117";
    private final static String DEFAULT_USER_EMAIL = "user1@yahoo.com";
    private final static String UPDATED_USER_EMAIL = "user2@yahoo.com";


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, passwordEncoder, modelMapper);
    }

    @Test
    @DisplayName(value = "Test get all users")
    public void testGetAllUsers() {
        final List<UserEntity> userEntities = List.of(
                createUserEntity(DEFAULT_USER_ID, DEFAULT_USER_EMAIL),
                createUserEntity("649348c9bb81557c80f11472117ee", UPDATED_USER_EMAIL)
        );
        when(userRepository.findAll()).thenReturn(userEntities);

        final Set<UserDto> users = userService.getAllUsers();

        assertEquals(2, users.size());
        for (final UserEntity userEntity : userEntities) {
            final UserDto userDto = modelMapper.map(userEntity, UserDto.class);
            assertTrue(users.contains(userDto));
        }
    }

    @Test
    @DisplayName(value = "Test get a user by existing id")
    public void testGetById_ExistingUserId() {
        final UserEntity userEntity = createUserEntity(DEFAULT_USER_ID, DEFAULT_USER_EMAIL);
        when(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.of(userEntity));

        final UserDto userDto = userService.getById(DEFAULT_USER_ID);

        assertNotNull(userDto);
        assertEquals(DEFAULT_USER_ID, userDto.getId());
    }

    @Test
    @DisplayName(value = "Test get a user by non existing id")
    public void testGetById_NonExistingUserId() {
        when(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.empty());

        assertThrows(SystemException.class, () -> userService.getById(DEFAULT_USER_ID));
    }

    @Test
    @DisplayName(value = "Test create a user")
    public void testCreate() {
        final User user = new User()
                .setEmail(DEFAULT_USER_EMAIL)
                .setPassword("password");
        final UserEntity savedUserEntity = new UserEntity()
                .setId(DEFAULT_USER_ID)
                .setEmail(DEFAULT_USER_EMAIL);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(savedUserEntity);

        final UserDto createdUserDto = userService.create(user);

        assertNotNull(createdUserDto);
        assertEquals(savedUserEntity.getId(), createdUserDto.getId());
        assertEquals(user.getEmail(), createdUserDto.getEmail());

        verify(userRepository).findByEmail(user.getEmail());
        verify(passwordEncoder).encode(user.getPassword());
        verify(userRepository).save(Mockito.any(UserEntity.class));
    }

    @Test
    @DisplayName(value = "Test update a user")
    public void testUpdate() {
        final User user = new User()
                .setId(DEFAULT_USER_ID)
                .setEmail(UPDATED_USER_EMAIL)
                .setPassword("newPassword");

        final UserEntity existingUserEntity = new UserEntity()
                .setId(DEFAULT_USER_ID)
                .setEmail(DEFAULT_USER_EMAIL);

        final UserEntity updatedUserEntity = new UserEntity()
                .setId(DEFAULT_USER_ID)
                .setEmail(UPDATED_USER_EMAIL);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(existingUserEntity));
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedNewPassword");
        when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(updatedUserEntity);

        final UserDto updatedUserDto = userService.update(user);

        assertNotNull(updatedUserDto);
        assertEquals(updatedUserEntity.getId(), updatedUserDto.getId());
        assertEquals(user.getEmail(), updatedUserDto.getEmail());

        verify(userRepository).findById(user.getId());
        verify(passwordEncoder).encode(user.getPassword());
        verify(userRepository).save(Mockito.any(UserEntity.class));
    }

    @Test
    @DisplayName(value = "Test remove an existing user")
    public void testRemove() {
        final UserEntity existingUserEntity = new UserEntity()
                .setId(DEFAULT_USER_ID)
                .setEmail(DEFAULT_USER_EMAIL);

        when(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.of(existingUserEntity));

        final UserDto removedUserDto = userService.remove(DEFAULT_USER_ID);

        assertNotNull(removedUserDto);
        assertEquals(existingUserEntity.getId(), removedUserDto.getId());

        verify(userRepository).findById(DEFAULT_USER_ID);
        verify(userRepository).deleteById(DEFAULT_USER_ID);
    }

    @Test
    @DisplayName(value = "Test remove non existing user")
    public void testRemove_UserNotFound() {
        when(userRepository.findById(DEFAULT_USER_ID)).thenReturn(Optional.empty());

        assertThrows(SystemException.class, () -> userService.remove(DEFAULT_USER_ID));

        verify(userRepository).findById(DEFAULT_USER_ID);
        verify(userRepository, Mockito.never()).deleteById(DEFAULT_USER_ID);
    }

    private UserEntity createUserEntity(String id, String email) {
        final UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setEmail(email);
        return userEntity;
    }
}