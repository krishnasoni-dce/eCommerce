package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void find_by_id() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        when(userRepository.findById(0L)).thenReturn(Optional.of(user));

        final ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        User user1 = response.getBody();
        assertNotNull(user1);
        assertEquals(0, user1.getId());
        assertEquals("username", user1.getUsername());
        assertEquals("password", user1.getPassword());
    }

    @Test
    public void find_by_id_notfound() {
        when(userRepository.findById(0L)).thenReturn(Optional.empty());

        final ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void find_by_username() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        when(userRepository.findByUsername("user")).thenReturn(user);

        final ResponseEntity<User> response = userController.findByUserName("user");
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        User user1 = response.getBody();
        assertNotNull(user1);
        assertEquals(0, user1.getId());
        assertEquals("username", user1.getUsername());
        assertEquals("password", user1.getPassword());
    }

    @Test
    public void find_by_username_not_found() {
        when(userRepository.findByUsername("testuser")).thenReturn(null);

        final ResponseEntity<User> response = userController.findByUserName("testuser");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void create_user() throws Exception {
        when(bCryptPasswordEncoder.encode("password")).thenReturn("hashedpassword");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("username");
        request.setPassword("password");
        request.setConfirmPassword("password");

        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        User testuser = response.getBody();
        assertNotNull(testuser);
        assertEquals(0, testuser.getId());
        assertEquals("username", testuser.getUsername());
        assertEquals("hashedpassword", testuser.getPassword());
    }

    @Test
    public void create_user_password_min_length_not_met() throws Exception {
        when(bCryptPasswordEncoder.encode("pass")).thenReturn("hashedpassword");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("username");
        request.setPassword("pass");
        request.setConfirmPassword("pass");

        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());

    }

    @Test
    public void create_user_password_dont_match() throws Exception {
        when(bCryptPasswordEncoder.encode("password")).thenReturn("hashedpassword");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("username");
        request.setPassword("password");
        request.setConfirmPassword("passWord");

        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
    }


}
