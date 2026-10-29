package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import model.UserData;
import service.RegisterRequest;
import service.RegisterResult;
import service.RegisterService;

public class RegisterHandler {
    static void handleRequest(Context context) throws Exception {
        var body = new Gson().fromJson(context.body(), UserData.class);
        RegisterRequest request = new RegisterRequest(body.username(), body.password(), body.email());
        RegisterResult result = RegisterService.register(request);
        context.status(200);
        context.json(new Gson().toJson(result));
    };
}
