package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    void clear();

    AuthData createAuth(UserData user);

    AuthData getAuth(String authToken) throws Exception;

    void deleteAuth(String authToken) throws Exception;
}
