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

    public int createGame(String gameName) throws Exception {
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
        games.add(new GameData(gameID, "", "", gameName));
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
        for (GameData game : games) {
            if (Objects.equals(gameID, game.gameID())) {
                game = update;
                return;
            }
        }
        throw new NotFoundException("bad request");
    }
}
