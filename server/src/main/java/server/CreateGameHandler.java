package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.CreateGameRequest;
import service.CreateGameResult;
import service.Service;


public class CreateGameHandler {
    static void handleCreateGame(Context context) throws Exception {
        var header = new Gson().fromJson(context.header("authorization"), String.class);
        Service.verifyAuth(header);
        var body = new Gson().fromJson(context.body(), CreateGameRequest.class);
        CreateGameResult result = Service.createGame(body);
        context.status(200);
        context.json(new Gson().toJson(Integer.toString(result.gameID())));
    }
}
