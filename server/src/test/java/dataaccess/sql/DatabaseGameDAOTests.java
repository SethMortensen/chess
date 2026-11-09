package dataaccess.sql;

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
    public void updateGameTest() throws Exception {
        DatabaseGameDAO gameDAO = new DatabaseGameDAO();
        gameDAO.clear();
        int gameID = gameDAO.createGame("game1");
        GameData game = gameDAO.getGame(gameID);
        GameData updatedGame = new GameData(gameID, "player1", null, "game1", game.game());
        gameDAO.updateGame(gameID, updatedGame);
        assert(Objects.equals(gameDAO.getGame(gameID).whiteUsername(), "player1"));
        gameDAO.clear();
    }
}
