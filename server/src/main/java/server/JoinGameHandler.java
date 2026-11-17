package server;

import chess.ChessBoard;
import com.google.gson.Gson;
import dataaccess.UnauthorizedException;
import io.javalin.http.Context;
import service.JoinGameRequest;
import service.Service;

public class JoinGameHandler {
    static void handleJoinGame(Context context) throws Exception {
        if(context.header("authorization").contains(" ")) {
            throw new UnauthorizedException("unauthorized");
        }
        var header = new Gson().fromJson(context.header("authorization"), String.class);
        var body = new Gson().fromJson(context.body(), JoinGameRequest.class);
        JoinGameRequest request = new JoinGameRequest(body.playerColor(), body.gameID());
        ChessBoard board = Service.joinGame(request, header);
        context.json(new Gson().toJson(board));
        context.status(200);
    }
}
