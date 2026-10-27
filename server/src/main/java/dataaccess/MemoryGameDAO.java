package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MemoryGameDAO implements GameDAO {
    ArrayList<GameData> games = new ArrayList<>();
    public void clear() {
        games.clear();
    }

    public void createGame(String gameName) throws DataAccessException {
        for (GameData game : games) {
            if (Objects.equals(gameName, game.gameName())) {
                throw new DataAccessException("Game already exists");
            }
        }
        Random rand = new Random();
        int gameID = rand.nextInt(9999);
        games.add(new GameData(gameID, null, null, gameName, new ChessGame()));
    }

    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData game : games) {
            if (Objects.equals(gameID, game.gameID())) {
                return game;
            }
        }
        throw new DataAccessException("Game not found");
    }

    public ArrayList<GameData> listGames() {
        return games;
    }

    public void updateGame(int gameID, GameData update) throws DataAccessException {
        for (GameData game : games) {
            if (Objects.equals(gameID, game.gameID())) {
                game = update;
                return;
            }
        }
        throw new DataAccessException("Game not found");
    }
}
