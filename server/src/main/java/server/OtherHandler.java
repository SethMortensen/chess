package server;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.NotFoundException;
import dataaccess.UnauthorizedException;
import io.javalin.http.Context;

import java.util.Map;

public class OtherHandler {
    static void handleAlreadyTaken(AlreadyTakenException alreadyTakenE, Context context) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", alreadyTakenE.getMessage())));
        context.status(403);
        context.json(body);
    }

    static void handleNotFound(NotFoundException notFoundE, Context context) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", notFoundE.getMessage())));
        context.status(400);
        context.json(body);
    }

    static void handleUnauthorized(UnauthorizedException unauthorizedE, Context context) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", unauthorizedE.getMessage())));
        context.status(401);
        context.json(body);
    }
}
