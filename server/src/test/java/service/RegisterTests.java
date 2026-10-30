package service;

import dataaccess.AlreadyTakenException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static service.Service.clear;
import static service.Service.register;

public class RegisterTests {
    @Test
    public void positiveRegisterTest() throws Exception {
        RegisterRequest request = new RegisterRequest("username", "password", "email");
        String expected = "username";
        assertEquals(expected, register(request).username(), "unexpected username");
        clear();
    }

    @Test
    public void negativeRegisterTest() throws Exception {
        RegisterRequest request = new RegisterRequest("username", "password", "email");
        register(request);
        assertThrows(AlreadyTakenException.class, () -> register(request), "did not throw exception when duplicate user made");
        clear();
    }
}
