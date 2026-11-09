package dataaccess.sql;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class DatabaseGameDAO implements GameDAO {
    public DatabaseGameDAO() throws DataAccessException {
        configureDatabase();
    }

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement("DELETE FROM games")) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to clear data");
        }
    }

    public int createGame(String gameName) throws DataAccessException {
        if (gameName == null) {
            throw new NotFoundException("bad request");
        }
        Random rand = new Random();
        int gameID = rand.nextInt(9999);
        ChessGame game = new ChessGame();
        String teamJson = new Gson().toJson(game.getTeamTurn());
        String boardJson = new Gson().toJson(game.getBoard());
        try (Connection conn = DatabaseManager.getConnection()) {
            if (checkIfTaken(gameName)) {
                throw new AlreadyTakenException("already taken");
            }
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, teamTurn, gameBoard) VALUES (?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, gameID);
                ps.setString(2, null);
                ps.setString(3, null);
                ps.setString(4, gameName);
                ps.setString(5, teamJson);
                ps.setString(6, boardJson);
                ps.executeUpdate();
                return gameID;
            }
        } catch (Exception e) {
            throw new UnauthorizedException("unauthorized");
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT whiteUsername, blackUsername, gameName, teamTurn, gameBoard FROM games WHERE gameID=?")) {
                ps.setInt(1, gameID);
                try(ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    String turnJson = rs.getString("teamTurn");
                    String boardJson = rs.getString("gameBoard");
                    ChessGame.TeamColor teamTurn = new Gson().fromJson(turnJson, ChessGame.TeamColor.class);
                    ChessBoard board = new Gson().fromJson(boardJson, ChessBoard.class);
                    ChessGame game = new ChessGame();
                    game.setTeamTurn(teamTurn);
                    game.setBoard(board);
                    return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                }
            }
        } catch (SQLException e) {
            throw new NotFoundException("bad request");
        }
    }

    public ArrayList<GameData> listGames() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM games")) {
                try (ResultSet rs = ps.executeQuery()) {
                    ArrayList<GameData> games = new ArrayList<>();
                    while (rs.next()) {
                        int gameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        String teamTurn = rs.getString("teamTurn");
                        String boardJson = rs.getString("gameBoard");
                        ChessBoard board = new Gson().fromJson(boardJson, ChessBoard.class);
                        ChessGame game = new ChessGame();
                        if (Objects.equals(teamTurn, "WHITE")) {
                            game.setTeamTurn(ChessGame.TeamColor.WHITE);
                        } else if (Objects.equals(teamTurn, "BLACK")) {
                            game.setTeamTurn(ChessGame.TeamColor.BLACK);
                        }
                        game.setBoard(board);
                        games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
                    }
                    return games;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to retrieve data");
        }
    }

    public void updateGame(int gameID, GameData update) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement dps = conn.prepareStatement("DELETE FROM games WHERE gameID=?")) {
                dps.setInt(1, gameID);
                dps.executeUpdate();
                try (PreparedStatement cps = conn.prepareStatement("INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, teamTurn, gameBoard) VALUES (?, ?, ?, ?, ?, ?)")) {
                    String turnJson = new Gson().toJson(update.game().getTeamTurn());
                    String boardJson = new Gson().toJson(update.game().getBoard());
                    cps.setInt(1, gameID);
                    cps.setString(2, update.whiteUsername());
                    cps.setString(3, update.blackUsername());
                    cps.setString(4, update.gameName());
                    cps.setString(5, turnJson);
                    cps.setString(6, boardJson);
                    cps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s", e.getMessage()));
        }
    }

    public boolean checkIfTaken(String gameName) throws Exception {
        var statement = "SELECT gameName FROM games WHERE gameName=?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, gameName);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }
    }

    private final String[] createStatements = {
            """
           CREATE TABLE IF NOT EXISTS games (
           `gameID` int NOT NULL,
           `whiteUsername` varchar(127),
           `blackUsername` varchar(127),
           `gameName` varchar(127) NOT NULL,
           `teamTurn` varchar(127) NOT NULL,
           `gameBoard` LONGTEXT NOT NULL
           )
           """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }
}
