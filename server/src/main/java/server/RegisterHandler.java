package server;

import com.google.gson.Gson;
import dataaccess.NotFoundException;
import io.javalin.http.Context;
import model.UserData;
import service.RegisterRequest;
import service.RegisterResult;
import service.Service;

public class RegisterHandler {
    static void handleRegister(Context context) throws Exception {
        var body = new Gson().fromJson(context.body(), UserData.class);
        if (body.username() == null || body.password() == null || body.email() == null) {
            throw new NotFoundException("bad request");
        }
        RegisterRequest request = new RegisterRequest(body.username(), body.password(), body.email());
        RegisterResult result = Service.register(request);
        context.status(200);
        context.json(new Gson().toJson(result));
    };
}
