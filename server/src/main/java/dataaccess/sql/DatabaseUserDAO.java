package dataaccess.sql;

import dataaccess.*;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;


public class DatabaseUserDAO implements UserDAO {
    public DatabaseUserDAO() throws DataAccessException {
        configureDatabase();
    }

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement("DELETE FROM users")) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to clear data");
        }
    }

    public void createUser(UserData u) throws Exception {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(u.password(), BCrypt.gensalt());
        try (Connection conn = DatabaseManager.getConnection()) {
            if (checkIfTaken(u.username())) {
                throw new AlreadyTakenException("already taken");
            }
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, u.username());
                ps.setString(2, hashedPassword);
                ps.setString(3, u.email());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to update database");
        }
    }

    public UserData getUser(String username, String password) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT password, email FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    String hashedPassword = rs.getString("password");
                    String email = rs.getString("email");
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        return new UserData(username, password, email);
                    }
                    throw new UnauthorizedException("unauthorized");
                }
            }
        } catch (SQLException e) {
            throw new UnauthorizedException("unauthorized");
        }
    }

    public boolean checkIfTaken(String username) throws Exception {
        var statement = "SELECT username FROM users WHERE username=?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }
    }

    private final String[] createStatements = {
            """
           CREATE TABLE IF NOT EXISTS users (
           `username` varchar(255) NOT NULL,
           `password` varchar(255) NOT NULL,
           `email` varchar(255) NOT NULL
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
