package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import service.LogoutRequest;
import service.Service;

public class LogoutHandler {
    static void handleLogout(Context context) throws Exception {
        var header = new Gson().fromJson(context.header("authorization"), String.class);
        LogoutRequest request = new LogoutRequest(header);
        Service.logout(request);
        context.status(200);
    }
}
