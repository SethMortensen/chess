package dataaccess.sql;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class DatabaseGameDAOTests {
    @Test
    public void createAndGetGameTest() throws Exception {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        int gameID = gameDAO.createGame("newGame");
        assert (gameDAO.getGame(gameID) != null);
        gameDAO.clear();
    }

    @Test
    public void listGamesTest() throws Exception {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();
        gameDAO.createGame("game1");
        gameDAO.createGame("game2");
        gameDAO.createGame("game3");
        assert(gameDAO.listGames().size() == 3);
        gameDAO.clear();
    }

    @Test
    public void updateGameJoinTest() throws Exception {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();
        int gameID = gameDAO.createGame("game1");
        GameData game = gameDAO.getGame(gameID);
        GameData updatedGame = new GameData(gameID, "player1", null, "game1", game.game());
        gameDAO.updateGame(gameID, updatedGame);
        assert(Objects.equals(gameDAO.getGame(gameID).whiteUsername(), "player1"));
        gameDAO.clear();
    }

    @Test
    public void updateGameMoveTests() throws Exception {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();
        int gameID = gameDAO.createGame("game1");
        GameData game = gameDAO.getGame(gameID);
        ChessGame updatedGame = game.game();
        updatedGame.makeMove(new ChessMove(new ChessPosition(2, 4), new ChessPosition(4, 4), null));
        gameDAO.updateGame(gameID, new GameData(gameID, null, null, "game1", updatedGame));
    }
}
