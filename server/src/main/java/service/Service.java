package service;

import chess.ChessBoard;
import chess.ChessGame;
import dataaccess.*;
import dataaccess.sql.DatabaseAuthDAO;
import dataaccess.sql.DatabaseGameDAO;
import dataaccess.sql.DatabaseUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Objects;

public class Service {
    static DatabaseAuthDAO authDAO;
    static DatabaseGameDAO gameDAO;
    static DatabaseUserDAO userDAO;
    static {
        try {
            userDAO = new DatabaseUserDAO();
            authDAO = new DatabaseAuthDAO();
            gameDAO = new DatabaseGameDAO();
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void verifyAuth(String authToken) throws Exception {
        authDAO.getAuth(authToken);
    }

    public static RegisterResult register(RegisterRequest registerRequest) throws Exception {
        UserData user = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        userDAO.createUser(user);
        AuthData authData = authDAO.createAuth(user);
        return new RegisterResult(registerRequest.username(), authData.authToken());
    }

    public static void clear() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    public static LoginResult login(LoginRequest request) throws Exception {
        UserData user = userDAO.getUser(request.username(), request.password());
        AuthData authToken = authDAO.createAuth(user);
        return new LoginResult(user.username(), authToken.authToken());
    }

    public static void logout(LogoutRequest request) throws Exception {
        String authToken = request.authToken();
        authDAO.deleteAuth(authToken);
    }

    public static CreateGameResult createGame(CreateGameRequest request) throws Exception {
        int gameID = gameDAO.createGame(request.gameName());
        return new CreateGameResult(gameID);
    }

    public static ListGamesResult listGames() throws DataAccessException {
        return new ListGamesResult(gameDAO.listGames());
    }

    public static ChessBoard joinGame(JoinGameRequest request, String authToken) throws Exception {
        verifyAuth(authToken);
        GameData game = gameDAO.getGame(request.gameID());
        GameData newGame;
        AuthData auth = authDAO.getAuth(authToken);
        if (Objects.equals(request.playerColor(), ChessGame.TeamColor.WHITE)) {
            if (!Objects.equals(game.whiteUsername(), null)) {
                throw new AlreadyTakenException("already taken");
            }
            newGame = new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game());
        } else if (Objects.equals(request.playerColor(), ChessGame.TeamColor.BLACK)){
            if (!Objects.equals(game.blackUsername(), null) && !Objects.equals(game.blackUsername(), "")) {
                throw new AlreadyTakenException("already taken");
            }
            newGame = new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game());
        } else {
            throw new NotFoundException("bad request");
        }
        gameDAO.updateGame(game.gameID(), newGame);
        return game.game().getBoard();
    }
}
