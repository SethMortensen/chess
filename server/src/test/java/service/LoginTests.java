package service;

import dataaccess.NotFoundException;
import dataaccess.UnauthorizedException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static service.Service.login;
import static service.Service.register;

public class LoginTests {
    @Test
    public void positiveLoginTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        register(regRequest);
        LoginRequest request = new LoginRequest("username", "password");
        assertEquals("username", login(request).username());
        Service.clear();
    }

    @Test
    public void wrongPasswordLoginTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        register(regRequest);
        LoginRequest request = new LoginRequest("username", "blubblub");
        assertThrows(UnauthorizedException.class, () -> login(request));
        Service.clear();
    }

    @Test
    public void wrongUsernameLoginTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        register(regRequest);
        LoginRequest request = new LoginRequest("blubblub", "password");
        assertThrows(NotFoundException.class, () -> login(request));
        Service.clear();
    }
}
