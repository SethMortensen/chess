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

    public void createGame(String gameName) throws Exception {
        for (GameData game : games) {
            if (Objects.equals(gameName, game.gameName())) {
                throw new AlreadyTakenException("Game already exists");
            }
        }
        Random rand = new Random();
        int gameID = rand.nextInt(9999);
        games.add(new GameData(gameID, null, null, gameName, new ChessGame()));
    }

    public GameData getGame(int gameID) throws Exception {
        for (GameData game : games) {
            if (Objects.equals(gameID, game.gameID())) {
                return game;
            }
        }
        throw new NotFoundException("Game not found");
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
        throw new NotFoundException("Game not found");
    }
}
