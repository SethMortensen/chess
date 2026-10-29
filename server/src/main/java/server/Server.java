package server;

import dataaccess.AlreadyTakenException;
import dataaccess.NotFoundException;
import dataaccess.UnauthorizedException;
import io.javalin.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", RegisterHandler::handleRequest);
        javalin.exception(AlreadyTakenException.class, OtherHandler::handleAlreadyTaken);
        javalin.exception(NotFoundException.class, OtherHandler::handleNotFound);
        javalin.exception(UnauthorizedException.class, OtherHandler::handleUnauthorized);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
