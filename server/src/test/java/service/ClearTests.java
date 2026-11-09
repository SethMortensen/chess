package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static service.Service.register;

public class ClearTests {
    @Test
    public void clearTest() throws Exception {
        Service.clear();
        RegisterRequest request = new RegisterRequest("username", "password", "email");
        RegisterResult result = register(request);
        Service.clear();
        assertThrows(DataAccessException.class, () -> Service.userDAO.getUser("username", "password"));
        assertThrows(DataAccessException.class, () -> Service.authDAO.getAuth(result.authToken()));
        assertThrows(DataAccessException.class, () -> Service.gameDAO.getGame(1));
    }
}
