package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    void clear() throws DataAccessException;

    AuthData createAuth(UserData user) throws DataAccessException;

    AuthData getAuth(String authToken) throws Exception;

    void deleteAuth(String authToken) throws Exception;
}
