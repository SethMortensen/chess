package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clear();

    int createGame(String gameName) throws Exception;

    GameData getGame(int gameID) throws Exception;

    ArrayList<GameData> listGames();

    void updateGame(int gameID, GameData update) throws Exception;
}
