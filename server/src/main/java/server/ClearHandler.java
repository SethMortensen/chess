package server;

import io.javalin.http.Context;
import service.Service;

public class ClearHandler {
    static void handleClear(Context context) {
        Service.clear();
        context.status(200);
    }
}
