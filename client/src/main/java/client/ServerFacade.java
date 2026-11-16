package client;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ServerFacade {
    private static final HttpClient httpClient = HttpClient.newHttpClient();

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
                System.err.println("Username, Password, and Email are required.");
            } else if (jsonResponse.statusCode() == 403) {
                System.err.println("Username or Email already in use");
            } else if (jsonResponse.statusCode() >= 500) {
                System.err.println("An unknown error occurred");
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.err.print("An error occurred with the server");
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
                System.err.println("Username and Password are required");
            } else if (jsonResponse.statusCode() == 401) {
                System.err.println("Incorrect Username or Password");
            } else if (jsonResponse.statusCode() >= 500) {
                System.err.println("An unknown error occurred");
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.err.print("An error occurred with the server");
        }
    }
}
