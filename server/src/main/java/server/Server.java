package server;

import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clearData)
                .post("/session", this::login)
                .delete("/session", this::logout)
                .get("/game", this::listGames)
                .post("/game", this::newGame)
                .put("/game", this::joinGame)
                .post("/user", this::register);

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

    private void clearData(Context context) {
        context.status(500);
        context.json(serverErrorJson);
    }
    private void joinGame(Context context) {
        String token = context.header("authToken");
        if (token == null || token.isEmpty()) {
            context.status(401);
            context.json(unauthorizedJson);
        } else {
            context.status(500);
            context.json(serverErrorJson);
        }
    }
    private void newGame(Context context) {
        String token = context.header("authToken");
        if (token == null || token.isEmpty()) {
            context.status(401);
            context.json(unauthorizedJson);
        } else {
            context.status(500);
            context.json(serverErrorJson);
        }
    }
    private void listGames(Context context) {
        String token = context.header("authToken");
        if (token == null || token.isEmpty()) {
            context.status(401);
            context.json(unauthorizedJson);
        } else {
            context.status(500);
            context.json(serverErrorJson);
        }
    }
    private void logout(Context context) {
        String token = context.header("authToken");
        if (token == null || token.isEmpty()) {
            context.status(401);
            context.json(unauthorizedJson);
        } else {
            context.status(500);
            context.json(serverErrorJson);
        }
    }
    private void login(Context context) {
        context.status(500);
        context.json(serverErrorJson);
    }
    private void register(Context context) {
        context.status(500);
        context.json(serverErrorJson);
    }
}
