package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clear();

    void createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    ArrayList<GameData> listGames();

    void updateGame(int gameID, GameData update) throws DataAccessException;
}
