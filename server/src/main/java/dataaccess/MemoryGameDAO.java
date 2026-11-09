package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MemoryGameDAO implements GameDAO {
    private final ArrayList<GameData> games;

    public MemoryGameDAO(ArrayList<GameData> games) {
        this.games = games;
    }
    public void clear() {
        games.clear();
    }

    public int createGame(String gameName) throws DataAccessException {
        if (gameName == null) {
            throw new NotFoundException("bad request");
        }
        for (GameData game : games) {
            if (Objects.equals(game.gameName(), gameName)) {
                throw new NotFoundException("bad request");
            }
        }
        if (Objects.equals(gameName, "")) {
            throw new NotFoundException("bad request");
        }
        Random rand = new Random();
        int gameID = rand.nextInt(9999);
        ChessGame game = new ChessGame();
        games.add(new GameData(gameID, null, null, gameName, game));
        return gameID;
    }

    public GameData getGame(int gameID) throws Exception {
        for (GameData game : games) {
            if (Objects.equals(gameID, game.gameID())) {
                return game;
            }
        }
        throw new NotFoundException("bad request");
    }

    public ArrayList<GameData> listGames() {
        return games;
    }

    public void updateGame(int gameID, GameData update) throws Exception {
        GameData game = getGame(gameID);
        games.remove(game);
        games.add(update);
    }
}
