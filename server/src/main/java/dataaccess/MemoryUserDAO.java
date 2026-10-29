package dataaccess;

import model.UserData;
import java.util.ArrayList;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    private final ArrayList<UserData> users;

    public MemoryUserDAO(ArrayList<UserData> users) {
        this.users = users;
    };
    public void clear() {
        users.clear();
    }

    public void createUser(UserData u) throws Exception {
        if (!users.isEmpty()) {
            if (users.contains(u)) {
                throw new AlreadyTakenException("already taken");
            }
            for (UserData user : users) {
                if (Objects.equals(user.username(), u.username())) {
                    throw new AlreadyTakenException("already taken");
                }
                if (Objects.equals(user.email(), u.email())) {
                    throw new AlreadyTakenException("already taken");
                }
            }
        }
        users.add(u);
    }

    public UserData getUser(String username, String password) throws Exception {
        if (users.isEmpty()) {
            throw new NotFoundException("bad request");
        }
        for (UserData user : users) {
            if (Objects.equals(user.username(), username) && (Objects.equals(user.password(), password))) {
                return user;
            }
        }
        throw new NotFoundException("bad request");
    }

}
