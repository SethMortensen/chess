package server;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import service.Service;

public class ClearHandler {
    static void handleClear(Context context) throws DataAccessException {
        Service.clear();
        context.status(200);
    }
}
