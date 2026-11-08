package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseAuthDAO implements AuthDAO {
    public DatabaseAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement("DELETE FROM auths")) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to clear data");
        }
    }

    public AuthData createAuth(UserData user) throws DataAccessException {
        var statement = "INSERT INTO auths (authtoken, username) VALUES (?, ?)";
        String token = generateToken();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, token);
                ps.setString(2, user.username());
                ps.executeUpdate();
                return new AuthData(token, user.username());
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to update database");
        }
    }

    private final String[] createStatements = {
           """
           CREATE TABLE IF NOT EXISTS auths (
           `authToken` varchar(255) NOT NULL,
           `username` varchar(255) NOT NULL
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
            throw new DataAccessException("Unable to configure database");
        }
    }
}
