package client;

import chess.ChessBoard;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import ui.PreloginUI;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ServerFacade {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private AuthData auth;
    private int gameCount = 0;
    int port;

    public ServerFacade(int port) {
        this.port = port;
    }

    public void quit() {
        if (auth != null) {
            logout();
        }
        httpClient.close();
    }

    public AuthData register(String username, String password, String email) {
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
                return auth;
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            PreloginUI.throwError("An error occurred with the server");
        }
        return null;
    }

    public AuthData login(String username, String password) {
        String urlString = String.format("http://localhost:%d/session", port);
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
                return auth;
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            PreloginUI.throwError("An error occurred with the server");
        }
        return null;
    }

    public void logout() {
        String urlString = String.format("http://localhost:%d/session", port);
        if (auth == null) {
            PreloginUI.throwError("You must be logged in to logout");
            return;
        }
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

    public ListGamesResult listGames() {
        String urlString = String.format("http://localhost:%d/game", port);
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
                        return null;
                    } else {
                        return response;
                    }
                }
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            PreloginUI.throwError("An error occurred with the server");
        }
        return null;
    }

    public CreateGameResult createGame(String gameName) {
        String urlString = String.format("http://localhost:%d/game", port);
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
                return new Gson().fromJson(jsonResponse.body(), CreateGameResult.class);
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            PreloginUI.throwError("An error occurred with the server");
        }
        return null;
    }

    public ChessBoard joinGame(int gameID, String color) {
        String urlString = String.format("http://localhost:%d/game", port);
        var body = Map.of("playerColor", color, "gameID", gameID);
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
                return new Gson().fromJson(jsonResponse.body(), ChessBoard.class);
            }
        } catch(URISyntaxException | IOException | InterruptedException e) {
            PreloginUI.throwError("An error occurred with the server");
            return null;
        }
        return null;
    }

    public ChessBoard spectateGame(int gameID) {
        ListGamesResult games = listGames();
        for (GameData game : games.games()) {
            if (game.gameID() == gameID) {
                return game.game().getBoard();
            }
        }
        return null;
    }
}
