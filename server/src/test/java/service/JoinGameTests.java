package service;

import chess.ChessGame;
import dataaccess.AlreadyTakenException;
import dataaccess.NotFoundException;
import dataaccess.UnauthorizedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static service.Service.*;

public class JoinGameTests {
    @Test
    public void positiveJoinGameTest() throws Exception {
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
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        RegisterResult regResult = register(regRequest);
        CreateGameResult createResult = createGame(new CreateGameRequest("newGame"));
        JoinGameRequest request = new JoinGameRequest(ChessGame.TeamColor.WHITE, createResult.gameID());
        joinGame(request, regResult.authToken());
        assertThrows(AlreadyTakenException.class, () -> joinGame(request, regResult.authToken()));
        clear();
    }

    @Test
    public void badRequestJoinGameTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        RegisterResult regResult = register(regRequest);
        createGame(new CreateGameRequest("newGame"));
        JoinGameRequest request = new JoinGameRequest(ChessGame.TeamColor.WHITE, 10000);
        assertThrows(NotFoundException.class, () -> joinGame(request, regResult.authToken()));
        clear();
    }

    @Test
    public void unauthorizedJoinGameTest() throws Exception {
        RegisterRequest regRequest = new RegisterRequest("username", "password", "email");
        RegisterResult regResult = register(regRequest);
        CreateGameResult createResult = createGame(new CreateGameRequest("newGame"));
        JoinGameRequest request = new JoinGameRequest(ChessGame.TeamColor.WHITE, createResult.gameID());
        assertThrows(UnauthorizedException.class, () -> joinGame(request, "badAuth"));
        clear();
    }
}
