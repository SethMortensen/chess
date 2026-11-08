package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static service.Service.*;

public class JoinGameTests {
    @Test
    public void positiveJoinGameTest() throws Exception {
        clear();
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        RegisterResult regResult = register(regRequest);
        CreateGameResult createResult = createGame(new CreateGameRequest("newGame"));
        JoinGameRequest request = new JoinGameRequest(ChessGame.TeamColor.WHITE, createResult.gameID());
        joinGame(request, regResult.authToken());
        assertEquals("username", listGames().games().getFirst().whiteUsername());
        clear();
    }

    @Test
    public void alreadyTakenJoinGameTest() throws Exception {
        clear();
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        RegisterResult regResult = register(regRequest);
        CreateGameResult createResult = createGame(new CreateGameRequest("newGame"));
        JoinGameRequest request = new JoinGameRequest(ChessGame.TeamColor.WHITE, createResult.gameID());
        joinGame(request, regResult.authToken());
        assertThrows(DataAccessException.class, () -> joinGame(request, regResult.authToken()));
        clear();
    }

    @Test
    public void badRequestJoinGameTest() throws Exception {
        clear();
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        RegisterResult regResult = register(regRequest);
        createGame(new CreateGameRequest("newGame"));
        JoinGameRequest request = new JoinGameRequest(ChessGame.TeamColor.WHITE, 10000);
        assertThrows(DataAccessException.class, () -> joinGame(request, regResult.authToken()));
        clear();
    }

    @Test
    public void unauthorizedJoinGameTest() throws Exception {
        clear();
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        CreateGameResult createResult = createGame(new CreateGameRequest("newGame"));
        JoinGameRequest request = new JoinGameRequest(ChessGame.TeamColor.WHITE, createResult.gameID());
        assertThrows(DataAccessException.class, () -> joinGame(request, "badAuth"));
        clear();
    }
}
