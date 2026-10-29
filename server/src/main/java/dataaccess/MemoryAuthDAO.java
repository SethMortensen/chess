package dataaccess;

import model.AuthData;
import model.UserData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAO {
    private final ArrayList<AuthData> auths;

    public MemoryAuthDAO(ArrayList<AuthData> auths) {
        this.auths = auths;
    }
    public void clear() {
        auths.clear();
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData createAuth(@NotNull UserData user) {
        AuthData auth = new AuthData(generateToken(), user.username());
        auths.add(auth);
        return auth;
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        for (AuthData data : auths) {
            if (Objects.equals(authToken, data.authToken())) {
                return data;
            }
        }
        throw new DataAccessException("Auth not found");
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        for (AuthData data : auths) {
            if (data.authToken().equals(authToken)) {
                auths.remove(data);
                return;
            }
        }
        throw new DataAccessException("Auth not found");
    }
}
