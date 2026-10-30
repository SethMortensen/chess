package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.ListGamesResult;
import service.Service;

public class ListGamesHandler {
    static void handleListGames(Context context) throws Exception {
        var header = new Gson().fromJson(context.header("authorization"), String.class);
        Service.verifyAuth(header);
        ListGamesResult games = Service.listGames();
        context.status(200);
        context.json(new Gson().toJson(games));
    }
}
