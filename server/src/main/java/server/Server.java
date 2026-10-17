package server;

import io.javalin.*;
import io.javalin.http.Context;

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

    private void exampleHandler(Context context) {}
    private void clearData(Context context) {}
    private void joinGame(Context context) {}
    private void newGame(Context context) {}
    private void listGames(Context context) {}
    private void logout(Context context) {}
    private void login(Context context) {}
    private void register(Context context) {}
}
