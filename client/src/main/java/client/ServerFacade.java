package client;

import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Map;

public class ServerFacade {

    static private String currentAuth = null;
    static private String currentUser = null;
    static private String serverHost;
    static private boolean viewing = false;
    static private boolean playing = false;
    static private final Gson serializer = new Gson();
    private final HttpClient client = HttpClient.newHttpClient();
    static private String serverMessage = null;

    private enum Method {
        GET,
        POST,
        PUT,
        DELETE
    }

    ServerFacade(String host) {
        serverHost = host;
    }

    public boolean clearServerState() {
        try {
            HttpRequest request = makeRequest("/db", Method.DELETE);
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                serverMessage = "Successfully cleared all game and user data";
                return true;
            }
            serverMessage = "Failed to clear game and user data";
            return false;
        }
        catch (IOException | InterruptedException e) {
            serverMessage = "Unable to reach the server. Please check your network connection.";
            return false;
        }
    }

    public boolean loginUser(String username, String password) { return false; }

    public boolean logoutUser() { return false; }

    public boolean registerUser(String username, String password, String email) { return false; }

    public boolean joinGame(String name, String color) { return false; }

    public GameData enterGame(String name) { return null; }

    public boolean createGame(String name) { return false; }

    public String getCurrentUser() { return(currentUser);}

    public boolean isLoggedIn() { return(currentAuth != null); }

    public List<GameData> listGames() { return(null); }

    public boolean isViewing() { return(viewing); }

    public boolean isPlaying() { return(playing); }

    private HttpRequest makeRequest(String path, Method method) {
        try {
            URI uri = new URI(serverHost + path);
            HttpRequest request;
            if (method == Method.GET) {
                request = HttpRequest.newBuilder()
                        .uri(uri)
                        .GET()
                        .build();
            }
            else if (method == Method.DELETE) {
                request = HttpRequest.newBuilder()
                        .uri(uri)
                        .DELETE()
                        .build();
            }
            else {
                serverMessage = "Uh oh, looks like you're missing some info. Please try again.";
                return(null);
            }
            return(request);
        }
        catch (URISyntaxException e) {
            serverMessage = "Hmmm... that server doesn't look right. Please check your configuration.";
            return null;
        }
    }

    private HttpRequest makeRequest(String path, Method method, Map<String, String> jsonMap) {
        try {
            URI uri = new URI(serverHost + path);
            HttpRequest request;
            String jsonString = serializer.toJson(jsonMap);

            if (method == Method.PUT) {
                request = HttpRequest.newBuilder()
                        .uri(uri)
                        .PUT(HttpRequest.BodyPublishers.ofString(jsonString))
                        .build();
            }
            else if (method == Method.POST) {
                request = HttpRequest.newBuilder()
                        .uri(uri)
                        .GET()
                        .build();
            }
            else {
                serverMessage = "Invalid action!";
                return null;
            }
            return request;
        }
        catch (URISyntaxException e) {
            serverMessage = "Hmmm... that server doesn't look right. Please check your configuration.";
            return null;
        }
    }
}
