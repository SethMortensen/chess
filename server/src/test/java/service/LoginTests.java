package service;


import dataaccess.DataAccessException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static service.Service.login;
import static service.Service.register;

public class LoginTests {
    @Test
    public void positiveLoginTest() throws Exception {
        Service.clear();
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        register(regRequest);
        LoginRequest request = new LoginRequest("username", "password");
        assertEquals("username", login(request).username());
        Service.clear();
    }

    @Test
    public void wrongPasswordLoginTest() throws Exception {
        Service.clear();
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        register(regRequest);
        LoginRequest request = new LoginRequest("username", "blubblub");
        assertThrows(DataAccessException.class, () -> login(request));
        Service.clear();
    }

    @Test
    public void wrongUsernameLoginTest() throws Exception {
        Service.clear();
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        register(regRequest);
        LoginRequest request = new LoginRequest("blubblub", "password");
        assertThrows(DataAccessException.class, () -> login(request));
        Service.clear();
    }
}
