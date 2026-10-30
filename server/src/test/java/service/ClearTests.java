package service;

import dataaccess.NotFoundException;
import dataaccess.UnauthorizedException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static service.Service.register;

public class ClearTests {
    @Test
    public void clearTest() throws Exception {
        RegisterRequest request = new RegisterRequest("username", "password", "email");
        RegisterResult result = register(request);
        Service.clear();
        assertThrows(UnauthorizedException.class, () -> Service.userDAO.getUser("username", "password"));
        assertThrows(UnauthorizedException.class, () -> Service.authDAO.getAuth(result.authToken()));
    }
}
