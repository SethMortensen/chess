package client;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import service.ListGamesResult;
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
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static AuthData auth;

    public static void quit() {
        httpClient.close();
    }

    public static void register(String username, String password, String email) {
        String urlString = "http://localhost:8080/user";
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
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            PreloginUI.throwError("An error occurred with the server");
        }
    }

    public static void login(String username, String password) {
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
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            PreloginUI.throwError("An error occurred with the server");
        }
    }

    public static void logout() {
        String urlString = "http://localhost:8080/session";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .timeout(java.time.Duration.ofMillis(5000))
                    .DELETE()
                    .header("authorization", auth.authToken())
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

    public static void listGames() {
        String urlString = "http://localhost:8080/game";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(urlString))
                    .timeout(java.time.Duration.ofMillis(5000))
                    .GET()
                    .header("authorization", auth.authToken())
                    .build();
            var jsonResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (jsonResponse.statusCode() == 401) {
                PreloginUI.throwError("You must be logged in to list games");
            } else if (jsonResponse.statusCode() >= 500) {
                PreloginUI.throwError("An unknown error occurred");
            } else if (jsonResponse.statusCode() == 200) {
                if (!jsonResponse.body().isEmpty()) {
                    var response = new Gson().fromJson(jsonResponse.body(), ListGamesResult.class);
                    int index = 1;
                    for (GameData game : response.games()) {
                        PostloginUI.listGame(index, game);
                        index++;
                    }
                }
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            PreloginUI.throwError("An error occurred with the server");
        }
    }
}
