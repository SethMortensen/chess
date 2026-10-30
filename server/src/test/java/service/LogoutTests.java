package service;

import dataaccess.UnauthorizedException;
import org.junit.jupiter.api.Test;

import static dataaccess.MemoryAuthDAO.generateToken;
import static org.junit.jupiter.api.Assertions.*;
import static service.Service.*;

public class LogoutTests {
    @Test
    public void positiveLogoutTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        RegisterResult regResult = register(regRequest);
        logout(new LogoutRequest(regResult.authToken()));
        assertThrows(UnauthorizedException.class, () -> authDAO.getAuth(regResult.authToken()));
        Service.clear();
    }

    @Test
    public void negativeLogoutTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        register(regRequest);
        assertThrows(UnauthorizedException.class, () -> logout(new LogoutRequest(generateToken())));
        Service.clear();
    }
}
