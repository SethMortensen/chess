package service;

import dataaccess.NotFoundException;
import dataaccess.UnauthorizedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static service.Service.*;

public class CreateGameTests {
    @Test
    public void positiveCreateGameTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        RegisterResult regResult = register(regRequest);
        verifyAuth(regResult.authToken());
        CreateGameResult result = createGame(new CreateGameRequest("gameID"));
        assertNotNull(result);
        clear();
    }

    @Test
    public void unauthorizedCreateGameTest() {
        assertThrows(UnauthorizedException.class, () -> verifyAuth("wrongAuthToken"));
        clear();
    }

    @Test
    public void badRequestCreateGameTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        register(regRequest);
        createGame(new CreateGameRequest("gameID"));
        assertThrows(NotFoundException.class, () -> createGame(new CreateGameRequest("gameID")));
        clear();
    }
}
