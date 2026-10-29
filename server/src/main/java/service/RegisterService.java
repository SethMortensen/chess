package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;

public class RegisterService {
    static MemoryUserDAO userDAO = new MemoryUserDAO(new ArrayList<UserData>());
    static MemoryAuthDAO authDAO = new MemoryAuthDAO(new ArrayList<AuthData>());
    public static RegisterResult register(RegisterRequest registerRequest) throws Exception {
        UserData user = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        userDAO.createUser(user);
        AuthData authData = authDAO.createAuth(user);
        return new RegisterResult(registerRequest.username(), authData.authToken());
    }
}
