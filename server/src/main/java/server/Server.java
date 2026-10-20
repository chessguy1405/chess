package server;

import chess.ChessGame;
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
        String token = context.header("authorization");
        if (token == null || token.isEmpty()) {
            context.status(401);
            context.json(unauthorizedJson);
        } else {
            try {
                String body = context.body();
                var bodyJson = serializer.fromJson(body, Map.class);
                String color = bodyJson.get("playerColor").toString();
                ChessGame.TeamColor pieceColor;
                if (color.equals("WHITE")) { pieceColor = ChessGame.TeamColor.WHITE; }
                else if (color.equals("BLACK")) {pieceColor = ChessGame.TeamColor.BLACK; }
                else { throw new BadColorException("Invalid color, must be one of [WHITE, BLACK]"); }
                int gameId = Integer.parseInt(bodyJson.get("gameID").toString());
                JoinGameRequest request = new JoinGameRequest(token, pieceColor, gameId);
                JoinGameService.joinGame(request);
                context.status(200);
                context.json(emptyJson);
            }
            catch (TeamTakenException e) {
                String alreadyTakenJson = serializer.toJson(Map.of("message", "Error: already taken"));
                context.status(403);
                context.json(alreadyTakenJson);
            }
            catch (DoesNotExistException e) {
                context.status(401);
                context.json(unauthorizedJson);
            }
            catch (NullPointerException | BadGameException | BadColorException | NumberFormatException e) {
                context.status(400);
                context.json(badRequestJson);
            }
            catch (Exception e) {
                context.status(500);
                context.json(serverErrorJson);
            }
        }
    }

    private void newGameHandler(Context context) {
        String token = context.header("authorization");
        if (token == null || token.isEmpty()) {
            context.status(401);
            context.json(unauthorizedJson);
        } else {
            try {
                String body = context.body();
                var bodyJson = serializer.fromJson(body, Map.class);
                String gameName = bodyJson.get("gameName").toString();
                NewGameRequest request = new NewGameRequest(token, gameName);
                NewGameResult result = CreateGameService.newGame(request);
                int gameId = result.gameID();
                String responseJson = serializer.toJson(Map.of("gameID", gameId));
                context.status(200);
                context.json(responseJson);
            }
            catch (NullPointerException e) {
                context.status(400);
                context.json(badRequestJson);
            }
            catch (DoesNotExistException e) {
                context.status(401);
                context.json(unauthorizedJson);
            }
            catch (Exception e) {
                context.status(500);
                context.json(serverErrorJson);
            }
        }
    }
    private void listGamesHandler(Context context) {
        String token = context.header("authorization");
        if (token == null || token.isEmpty()) {
            context.status(401);
            context.json(unauthorizedJson);
            return;
        }
        try {
            ListGameRequest request = new ListGameRequest(token);
            ListGameResult result = ListGameService.listGames(request);
            String gameListJson = serializer.toJson(result);
            context.status(200);
            context.json(gameListJson);
        }
        catch (DoesNotExistException e) {
            context.status(401);
            context.json(unauthorizedJson);
        }
        catch (Exception e) {
            context.status(500);
            context.json(serverErrorJson);
        }
    }

    private void logoutHandler(Context context) {
        String token = context.header("authorization");
        if (token == null || token.isEmpty()) {
            context.status(401);
            context.json(unauthorizedJson);
            return;
        }
        try {
            LogoutRequest request = new LogoutRequest(token);
            LogoutService.logout(request);
            context.status(200);
            context.json(emptyJson);
        }
        catch (DoesNotExistException e) {
            context.status(401);
            context.json(unauthorizedJson);
        }
        catch (Exception e) {
            context.status(500);
            context.json(serverErrorJson);
        }
    }

    private void loginHandler(Context context) {
        String invalidLoginJson = serializer.toJson(Map.of("message", "Error: unauthorized"));
        try {
            String body = context.body();
            var bodyJson = serializer.fromJson(body, Map.class);
            String username = bodyJson.get("username").toString();
            String password = bodyJson.get("password").toString();
            LoginRequest request = new LoginRequest(username, password);
            LoginResult result = LoginService.login(request);
            String resultJson = serializer.toJson(result);
            context.status(200);
            context.json(resultJson);
        }
        catch (BadPasswordException | DoesNotExistException e) {
            context.status(401);
            context.json(invalidLoginJson);
        }
        catch (NullPointerException e) {
            context.status(400);
            context.json(invalidLoginJson);
        }
        catch (Exception e) {
            context.status(500);
            context.json(serverErrorJson);
        }
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
            context.status(403);
            context.json(usernameTakenJson);
        }
        catch (NullPointerException e) {
            context.status(400);
            context.json(badRequestJson);
        }
        catch (Exception e) {
            context.status(500);
            context.json(serverErrorJson);
        }
    }
}
