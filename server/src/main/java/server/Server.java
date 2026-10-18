package server;

import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import java.util.Map;
import model.*;
import org.eclipse.jetty.server.Authentication;
import service.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clearDataHandler)
                .post("/session", this::loginHandler)
                .delete("/session", this::logoutHandler)
                .get("/game", this::listGamesHandler)
                .post("/game", this::newGameHandler)
                .put("/game", this::joinGameHandler)
                .post("/user", this::registerHandler);

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private final Gson serializer = new Gson();
    private final String unauthorizedJson = serializer.toJson(Map.of("message", "Error: Unauthorized"));
    private final String serverErrorJson = serializer.toJson(Map.of("message", "Error: Internal Server Error"));
    private final String badRequestJson = serializer.toJson(Map.of("message", "Error: bad request"));
    private final String notImplementedJson = serializer.toJson(Map.of("message", "Not Implemented"));
    private final String emptyJson = serializer.toJson(Map.of());

    private void clearDataHandler(Context context) {
        try {
            ClearDataService.clearAllData();
            context.status(200);
            context.json(emptyJson);
        }
        catch(Exception e) {
            context.status(500);
            context.json(serverErrorJson);
        }

    }
    private void joinGameHandler(Context context) {
        String token = context.header("authToken");
        if (token == null || token.isEmpty()) {
            context.status(401);
            context.json(unauthorizedJson);
        } else {
            context.status(501);
            context.json(notImplementedJson);
        }
    }
    private void newGameHandler(Context context) {
        String token = context.header("authToken");
        if (token == null || token.isEmpty()) {
            context.status(401);
            context.json(unauthorizedJson);
        } else {
            context.status(501);
            context.json(notImplementedJson);
        }
    }
    private void listGamesHandler(Context context) {
        String token = context.header("authToken");
        if (token == null || token.isEmpty()) {
            context.status(401);
            context.json(unauthorizedJson);
        } else {
            context.status(501);
            context.json(notImplementedJson);
        }
    }
    private void logoutHandler(Context context) {
        String token = context.header("authToken");
        if (token == null || token.isEmpty()) {
            context.status(401);
            context.json(unauthorizedJson);
        } else {
            context.status(501);
            context.json(notImplementedJson);
        }
    }
    private void loginHandler(Context context) {
        context.status(500);
        context.json(serverErrorJson);
    }
    private void registerHandler(Context context) {
        String usernameTakenJson = serializer.toJson(Map.of("message", "Error: username already taken"));
        try {
            String body = context.body();
            var bodyJson = serializer.fromJson(body, Map.class);
            String username = bodyJson.get("username").toString();
            String password = bodyJson.get("password").toString();
            String email = bodyJson.get("email").toString();
            UserData registerData = new UserData(username, password, email);
            LoginResult result = RegisterService.register(registerData);
            String resultJson = serializer.toJson(result);
            context.status(200);
            context.json(resultJson);
        }
        catch (UserExistsException e) {
            context.status(503);
            context.json(usernameTakenJson);
        }
        catch (Exception e) {
            context.status(400);
            context.json(badRequestJson);
            return;
        }
    }
}
