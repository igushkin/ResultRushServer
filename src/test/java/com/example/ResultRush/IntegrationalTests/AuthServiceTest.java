package com.example.ResultRush.IntegrationalTests;

import com.example.ResultRush.configurations.Jwt.JwtIssuer;
import com.example.ResultRush.configurations.UserPrincipal;
import com.example.ResultRush.entity.Category;
import com.example.ResultRush.entity.Usr;
import com.example.ResultRush.model.LoginResponse;
import com.example.ResultRush.service.AuthService;
import com.example.ResultRush.service.UserService;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.ResultRush.IntegrationalTests.util.InstanceCreator.createUser;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class AuthServiceTest {

    private final AuthService authService;
    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;

    @Test
    @WithMockCustomUser
    public void registerSuccess() {

        var username = "login";
        var password = "password";

        authService.register(username, password);

        TypedQuery<Usr> query = em.createQuery("Select u from Usr u where u.username = :username", Usr.class);
        var dbResult = query.setParameter("username", username).getSingleResult();
        Assertions.assertEquals(username, dbResult.getUsername());
        Assertions.assertTrue(passwordEncoder.matches(password, dbResult.getPassword()));
    }


    @Test
    @WithMockCustomUser
    public void registerFail() {
        Assertions.assertThrows(Exception.class, () -> authService.register("  ", "password"));
        Assertions.assertThrows(Exception.class, () -> authService.register("username", " "));
    }

    @Test
    @WithMockCustomUser
    public void loginSuccess() {
        var user = createUser();
        var pass = user.getPassword();
        user.setPassword(passwordEncoder.encode(pass));
        em.persist(user);

        var result = authService.attemptLogin(user.getUsername(), pass);

        Assertions.assertTrue(!Strings.isBlank(result.getToken()));
    }

    @Test
    @WithMockCustomUser
    public void loginFail() {
        var user = createUser();
        var pass = user.getPassword();
        user.setPassword(passwordEncoder.encode(pass));
        em.persist(user);

        Assertions.assertThrows(Exception.class, () -> authService.attemptLogin(user.getUsername(), "wrong pass"));
    }
}