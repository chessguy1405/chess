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
            HttpRequest request = makeRequest("/db", Method.DELETE, false);
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

    public boolean loginUser(String username, String password) {
        try {
            var loginMap = Map.of("username", username, "password", password);
            HttpRequest request = makeRequest("/session", Method.POST, false, loginMap);
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                serverMessage = "Successfully logged in " + username;
                var responseBody = response.body();
                var responseJson = serializer.fromJson(responseBody, Map.class);
                currentUser = responseJson.get("username").toString();
                currentAuth = responseJson.get("authToken").toString();
                return true;
            }
            else if (response.statusCode() == 401) {
                serverMessage = "Username or password is incorrect";
                return false;
            }
            else if (response.statusCode() == 400) {
                serverMessage = "Username and password are required";
                return false;
            }
            else {
                serverMessage = "Failed to log in. An unknown error occurred. Please contact the developer.";
                return false;
            }
        }
        catch (IOException | InterruptedException e) {
            serverMessage = "Unable to reach the server. Please check your network connection.";
            return false;
        }
    }

    public boolean logoutUser() {
        if (currentAuth == null) {
            serverMessage = "Hmmm, looks like you're not logged in";
            return false;
        }
        else {
            try {
                HttpRequest request = makeRequest("/session", Method.DELETE, true);
                var response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    serverMessage = "Successfully logged out!";
                    currentUser = null;
                    currentAuth = null;
                    return true;
                } else if (response.statusCode() == 401) {
                    serverMessage = "Hmmm, looks like you're not logged in";
                    return false;
                } else {
                    serverMessage = "Failed to log out. An unknown error occurred. Please contact the developer.";
                    return false;
                }
            } catch (IOException | InterruptedException e) {
                serverMessage = "Unable to reach the server. Please check your network connection.";
                return false;
            }
        }
    }

    public boolean registerUser(String username, String password, String email) {
        try {
            var jsonMap = Map.of("username", username, "password", password, "email", email);
            HttpRequest request = makeRequest("/user", Method.POST, false, jsonMap);
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                serverMessage = "Successfully registered and logged in " + username;
                var responseBody = response.body();
                var responseJson = serializer.fromJson(responseBody, Map.class);
                currentUser = responseJson.get("username").toString();
                currentAuth = responseJson.get("authToken").toString();
                return true;
            }
            else if (response.statusCode() == 400) {
                serverMessage = "Failed to complete registration. Please make sure that you fill out all required values";
                return false;
            }
            else if (response.statusCode() == 403) {
                serverMessage = "Failed to register " + username + ". Please use a different username.";
                return false;
            }
            else {
                serverMessage = "Failed to register. An unknown error occurred. Please try again or contact the developer.";
                return false;
            }
        }
        catch (IOException | InterruptedException e) {
            serverMessage = "Unable to reach the server. Please check your network connection.";
            return false;
        }
    }

    public boolean joinGame(String name, String color) { return false; }

    public GameData enterGame(String name) { return null; }

    public boolean createGame(String name) { return false; }

    public String getCurrentUser() { return(currentUser);}

    public boolean isLoggedIn() { return(currentAuth != null); }

    public List<GameData> listGames() { return(null); }

    public boolean isViewing() { return(viewing); }

    public boolean isPlaying() { return(playing); }

    private HttpRequest makeRequest(String path, Method method, boolean needsAuth) {
        try {
            URI uri = new URI(serverHost + path);
            HttpRequest request;
            if (needsAuth) {
                if (method == Method.GET) {
                    request = HttpRequest.newBuilder()
                            .uri(uri)
                            .header("authorization", currentAuth)
                            .GET()
                            .build();
                } else if (method == Method.DELETE) {
                    request = HttpRequest.newBuilder()
                            .uri(uri)
                            .header("authorization", currentAuth)
                            .DELETE()
                            .build();
                } else {
                    serverMessage = "Uh oh, looks like you're missing some info. Please try again.";
                    return (null);
                }
            }
            else{
                if (method == Method.GET) {
                    request = HttpRequest.newBuilder()
                            .uri(uri)
                            .GET()
                            .build();
                } else if (method == Method.DELETE) {
                    request = HttpRequest.newBuilder()
                            .uri(uri)
                            .DELETE()
                            .build();
                } else {
                    serverMessage = "Uh oh, looks like you're missing some info. Please try again.";
                    return (null);
                }
            }
            return(request);
        }
        catch (URISyntaxException e) {
            serverMessage = "Hmmm... that server doesn't look right. Please check your configuration.";
            return null;
        }
    }

    private HttpRequest makeRequest(String path, Method method, boolean needsAuth, Map<String, String> jsonMap) {
        try {
            URI uri = new URI(serverHost + path);
            HttpRequest request;
            String jsonString = serializer.toJson(jsonMap);

            if (needsAuth) {
                if (method == Method.PUT) {
                    request = HttpRequest.newBuilder()
                            .uri(uri)
                            .header("authorization", currentAuth)
                            .PUT(HttpRequest.BodyPublishers.ofString(jsonString))
                            .build();
                }
                else if (method == Method.POST) {
                    request = HttpRequest.newBuilder()
                            .uri(uri)
                            .header("authorization", currentAuth)
                            .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                            .build();
                }
                else {
                    serverMessage = "Invalid action!";
                    return null;
                }
            }
            else {
                if (method == Method.PUT) {
                    request = HttpRequest.newBuilder()
                            .uri(uri)
                            .PUT(HttpRequest.BodyPublishers.ofString(jsonString))
                            .build();
                } else if (method == Method.POST) {
                    request = HttpRequest.newBuilder()
                            .uri(uri)
                            .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                            .build();
                } else {
                    serverMessage = "Invalid action!";
                    return null;
                }
            }
            return request;
        }
        catch (URISyntaxException e) {
            serverMessage = "Hmmm... that server doesn't look right. Please check your configuration.";
            return null;
        }
    }

    public String getServerMessage() { return(serverMessage); }
}
