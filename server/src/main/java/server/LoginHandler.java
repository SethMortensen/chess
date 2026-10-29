package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import model.UserData;
import service.LoginRequest;
import service.LoginResult;
import service.Service;

public class LoginHandler {
    static void handleLogin(Context context) throws Exception {
        var body = new Gson().fromJson(context.body(), UserData.class);
        LoginRequest request = new LoginRequest(body.username(), body.password());
        LoginResult result = Service.login(request);
        context.status(200);
        context.result(new Gson().toJson(result));
    }
}
