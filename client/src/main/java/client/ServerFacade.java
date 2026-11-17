package client;

import chess.ChessBoard;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import service.CreateGameResult;
import service.ListGamesResult;
import service.Service;
import ui.PostloginUI;
import ui.PreloginUI;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;

public class ServerFacade {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private AuthData auth;
    private final ArrayList<gameIDMap> idList = new ArrayList<>();
    private int gameCount = 0;
    int port;

    public ServerFacade(int port) {
        this.port = port;
    }

    public void quit() {
        httpClient.close();
    }

    public boolean register(String username, String password, String email) {
        String urlString = String.format("http://localhost:%d/user", port);
        var body = Map.of("username", username, "password", password, "email", email);
        var jsonBody = new Gson().toJson(body);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .timeout(java.time.Duration.ofMillis(5000))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            var jsonResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (jsonResponse.statusCode() == 400) {
                PreloginUI.throwError("Username, Password, and Email are required.");
            } else if (jsonResponse.statusCode() == 403) {
                PreloginUI.throwError("Username or Email already in use");
            } else if (jsonResponse.statusCode() >= 500) {
                PreloginUI.throwError("An unknown error occurred");
            } else if (jsonResponse.statusCode() == 200) {
                auth = new Gson().fromJson(jsonResponse.body(), AuthData.class);
                return true;
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            PreloginUI.throwError("An error occurred with the server");
        }
        return false;
    }

    public boolean login(String username, String password) {
        String urlString = "http://localhost:8080/session";
        var body = Map.of("username", username, "password", password);
        var jsonBody = new Gson().toJson(body);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .timeout(java.time.Duration.ofMillis(5000))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            var jsonResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (jsonResponse.statusCode() == 400) {
                PreloginUI.throwError("Username and Password are required");
            } else if (jsonResponse.statusCode() == 401) {
                PreloginUI.throwError("Incorrect Username or Password");
            } else if (jsonResponse.statusCode() >= 500) {
                PreloginUI.throwError("An unknown error occurred");
            } else if (jsonResponse.statusCode() == 200) {
                auth = new Gson().fromJson(jsonResponse.body(), AuthData.class);
                return true;
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            PreloginUI.throwError("An error occurred with the server");
        }
        return false;
    }

    public void logout() {
        String urlString = "http://localhost:8080/session";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .timeout(java.time.Duration.ofMillis(5000))
                    .header("authorization", auth.authToken())
                    .DELETE()
                    .build();
            var jsonResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (jsonResponse.statusCode() == 401) {
                PreloginUI.throwError("You must be logged in to logout");
            } else if (jsonResponse.statusCode() >= 500) {
                PreloginUI.throwError("An unknown error occurred");
            } else if (jsonResponse.statusCode() == 200) {
                auth = null;
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            PreloginUI.throwError("An error occurred with the server");
        }
    }

    public void listGames() {
        String urlString = "http://localhost:8080/game";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .timeout(java.time.Duration.ofMillis(5000))
                    .header("authorization", auth.authToken())
                    .GET()
                    .build();
            var jsonResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (jsonResponse.statusCode() == 401) {
                PreloginUI.throwError("You must be logged in to list games");
            } else if (jsonResponse.statusCode() >= 500) {
                PreloginUI.throwError("An unknown error occurred");
            } else if (jsonResponse.statusCode() == 200) {
                if (!jsonResponse.body().isEmpty()) {
                    var response = new Gson().fromJson(jsonResponse.body(), ListGamesResult.class);
                    if (gameCount == 0) {
                        PostloginUI.listNoGames();
                    } else {
                        for (GameData game : response.games()) {
                            for (gameIDMap idMap : idList) {
                                if (idMap.serverGameID() == game.gameID()) {
                                    PostloginUI.listGame(idMap.userGameID(), game);
                                }
                            }
                        }
                    }
                }
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            PreloginUI.throwError("An error occurred with the server");
        }
    }

    public void createGame(String gameName) {
        String urlString = "http://localhost:8080/game";
        var body = Map.of("gameName", gameName);
        var jsonBody = new Gson().toJson(body);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .timeout(java.time.Duration.ofMillis(5000))
                    .header("authorization", auth.authToken())
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            var jsonResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (jsonResponse.statusCode() == 400) {
                PreloginUI.throwError("Game name is required");
            } else if (jsonResponse.statusCode() == 401) {
                PreloginUI.throwError("You must be logged in to create a game");
            } else if (jsonResponse.statusCode() >= 500) {
                PreloginUI.throwError("An unknown error occurred");
            } else if (jsonResponse.statusCode() == 200) {
                gameCount++;
                var response = new Gson().fromJson(jsonResponse.body(), CreateGameResult.class);
                idList.add(new gameIDMap(gameCount, response.gameID()));
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            PreloginUI.throwError("An error occurred with the server");
        }
    }

    public ChessBoard joinGame(String gameID, String color) {
        String urlString = "http://localhost:8080/game";
        int serverGameID = -1;
        for (gameIDMap map : idList) {
            if (map.userGameID() == Integer.parseInt(gameID)) {
                serverGameID = map.serverGameID();
            }
        }
        if (serverGameID == -1) {
            PreloginUI.throwError("Invalid Game ID");
        }
        var body = Map.of("playerColor", color, "gameID", serverGameID);
        var jsonBody = new Gson().toJson(body);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .timeout(java.time.Duration.ofMillis(5000))
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .header("authorization", auth.authToken())
                    .build();
            var jsonResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (jsonResponse.statusCode() == 400) {
                PreloginUI.throwError("Game ID is required");
            } else if (jsonResponse.statusCode() == 401) {
                PreloginUI.throwError("You must be logged in to join a game");
            } else if (jsonResponse.statusCode() == 403) {
                PreloginUI.throwError("Color already taken");
            } else if (jsonResponse.statusCode() >= 500) {
                PreloginUI.throwError("An unknown error occurred");
            } else if (jsonResponse.statusCode() == 200) {
                return Service.getBoardFromID(serverGameID);
            }
        } catch(URISyntaxException | IOException | InterruptedException e) {
            PreloginUI.throwError("An error occurred with the server");
            return null;
        } catch(DataAccessException e) {
            PreloginUI.throwError("Invalid Game ID");
            return null;
        }
        return null;
    }

    public ChessBoard spectateGame(String gameID) {
        int serverGameID = -1;
        for (gameIDMap map : idList) {
            if (map.userGameID() == Integer.parseInt(gameID)) {
                serverGameID = map.serverGameID();
            }
        }
        if (serverGameID == -1) {
            PreloginUI.throwError("Invalid Game ID");
            return null;
        }
        try {
            return Service.getBoardFromID(serverGameID);
        } catch (DataAccessException e) {
            PreloginUI.throwError("Invalid Game ID");
            return null;
        }
    }
}
