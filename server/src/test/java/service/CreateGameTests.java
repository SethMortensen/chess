package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static service.Service.*;

public class CreateGameTests {
    @Test
    public void positiveCreateGameTest() throws Exception {
        clear();
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        RegisterResult regResult = register(regRequest);
        verifyAuth(regResult.authToken());
        CreateGameResult result = createGame(new CreateGameRequest("gameID"));
        assertNotNull(result);
        clear();
    }

    @Test
    public void unauthorizedCreateGameTest() throws Exception {
        clear();
        assertThrows(DataAccessException.class, () -> verifyAuth("wrongAuthToken"));
        clear();
    }

    @Test
    public void badRequestCreateGameTest() throws Exception {
        clear();
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        register(regRequest);
        createGame(new CreateGameRequest("gameID"));
        assertThrows(DataAccessException.class, () -> createGame(new CreateGameRequest("gameID")));
        clear();
    }
}
