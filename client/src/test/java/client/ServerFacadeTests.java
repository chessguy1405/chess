package client;

import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private Map<String, String> user1 = Map.of("username", "test", "password", "test", "email", "user1@example.com");
    private Map<String, String> user2 = Map.of("username", "quiz", "password", "quiz", "email", "user2@example.com");
    private Map<String, String> user3 = Map.of("username", "exam", "password", "exam", "email", "user3@example.com");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearServerState() {
        if (!facade.clearServerState()) {
            throw(new RuntimeException("Failed to clear server state"));
        }
        facade.logoutUser();
    }

    @Test
    public void registerTest() {
        Assertions.assertTrue(facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email")));
        Assertions.assertTrue(facade.isLoggedIn());
        Assertions.assertEquals("test", facade.getCurrentUser());
        Assertions.assertTrue(facade.registerUser(user2.get("username"), user2.get("password"), user2.get("email")));
        Assertions.assertTrue(facade.isLoggedIn());
        Assertions.assertEquals("quiz", facade.getCurrentUser());
        Assertions.assertTrue(facade.registerUser(user3.get("username"), user3.get("password"), user3.get("email")));
        Assertions.assertTrue(facade.isLoggedIn());
        Assertions.assertEquals("exam", facade.getCurrentUser());
    }

    @Test
    public void registerFailedTest() {
        Assertions.assertTrue(facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email")));
        Assertions.assertFalse(facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email")));
        Assertions.assertTrue(facade.isLoggedIn());
        Assertions.assertEquals("test", facade.getCurrentUser());
    }

    @Test
    public void logoutTest() {
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        Assertions.assertTrue(facade.logoutUser());
        Assertions.assertFalse(facade.isLoggedIn());
        Assertions.assertNull(facade.getCurrentUser());
    }

    @Test
    public void logoutFailedNoUser() {
        Assertions.assertFalse(facade.logoutUser());
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        Assertions.assertTrue(facade.logoutUser());
        Assertions.assertFalse(facade.isLoggedIn());
        Assertions.assertNull(facade.getCurrentUser());
        Assertions.assertFalse(facade.logoutUser());
    }

    @Test
    public void loginTest() {
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        facade.logoutUser();
        Assertions.assertTrue(facade.loginUser(user1.get("username"), user1.get("password")));
        Assertions.assertEquals("test", facade.getCurrentUser());
    }

    @Test
    public void loginFailedWrongPassword() {
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        facade.logoutUser();
        Assertions.assertFalse(facade.loginUser(user1.get("username"), user2.get("password")));
        Assertions.assertNull(facade.getCurrentUser());
        Assertions.assertFalse(facade.isLoggedIn());
    }

    @Test
    public void loginFailedWrongUser() {
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        facade.logoutUser();
        Assertions.assertFalse(facade.loginUser(user2.get("username"), user2.get("password")));
        Assertions.assertNull(facade.getCurrentUser());
        Assertions.assertFalse(facade.isLoggedIn());
    }

    @Test
    public void newGame() {
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        Assertions.assertTrue(facade.createGame("game1"));
        Assertions.assertTrue(facade.createGame("game2"));
        List<GameData> gameList = facade.listGames();
        Assertions.assertEquals(2, gameList.size());
        for (int i=0; i<2; i++) {
            boolean found = false;
            String gameName = "game" + String.valueOf(i);
            for (GameData game : gameList) {
                if (game.gameName().equals(gameName)) {
                    found = true;
                    break;
                }
            }
            Assertions.assertTrue(found);
        }
    }

    @Test
    public void newGameFailedSameName() {
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        Assertions.assertTrue(facade.createGame("game1"));
        Assertions.assertFalse(facade.createGame("game1"));
        List<GameData> gameList = facade.listGames();
        Assertions.assertEquals(1, gameList.size());
        GameData game = gameList.getFirst();
        Assertions.assertEquals("game1", game.gameName());
    }

    @Test
    public void newGameFailedNoUser() {
        Assertions.assertFalse(facade.createGame("game1"));
        List<GameData> gameList = facade.listGames();
        Assertions.assertEquals(0, gameList.size());
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        facade.logoutUser();
        Assertions.assertFalse(facade.createGame("game1"));
        gameList = facade.listGames();
        Assertions.assertEquals(0, gameList.size());
    }

    @Test
    public void joinGame() {
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        facade.createGame("game");
        Assertions.assertTrue(facade.joinGame("game", "white"));
        facade.registerUser(user2.get("username"), user2.get("password"), user2.get("email"));
        Assertions.assertTrue(facade.joinGame("game", "black"));
        List<GameData> gameList = facade.listGames();
        GameData game = gameList.getFirst();
        Assertions.assertEquals("test", game.whiteUsername());
        Assertions.assertEquals("quiz", game.blackUsername());
    }

    @Test
    public void joinGameFailedNoGame() {
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        Assertions.assertFalse(facade.joinGame("game", "white"));
    }

    @Test
    public void joinGameFailedNoUser() {
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        facade.createGame("game");
        facade.logoutUser();
        Assertions.assertFalse(facade.joinGame("game", "white"));
        facade.loginUser(user1.get("username"), user1.get("password"));
        List<GameData> gameList = facade.listGames();
        GameData game = gameList.getFirst();
        Assertions.assertNull(game.whiteUsername());
    }

    @Test
    public void joinGameFailedColorTaken() {
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        facade.createGame("game");
        Assertions.assertTrue(facade.joinGame("game", "white"));
        Assertions.assertTrue(facade.joinGame("game", "black"));
        facade.registerUser(user2.get("username"), user2.get("password"), user2.get("email"));
        Assertions.assertFalse(facade.joinGame("game", "white"));
        Assertions.assertFalse(facade.joinGame("game", "black"));
        List<GameData> gameList = facade.listGames();
        GameData game = gameList.getFirst();
        Assertions.assertEquals("test", game.whiteUsername());
        Assertions.assertEquals("test", game.whiteUsername());
    }

    @Test
    public void observeGame() {
        createAndJoin();
        facade.loginUser(user3.get("username"), user3.get("password"));
        List<GameData> gameList = facade.listGames();
        GameData game = gameList.getFirst();
        Assertions.assertEquals(game, facade.enterGame("game"));
        Assertions.assertTrue(facade.isViewing());
        Assertions.assertFalse(facade.isPlaying());
    }

    @Test
    public void enterGameAsWhite() {
        createAndJoin();
        facade.loginUser(user1.get("username"), user1.get("password"));
        List<GameData> gameList = facade.listGames();
        GameData game = gameList.getFirst();
        Assertions.assertEquals(game, facade.enterGame("game"));
        Assertions.assertTrue(facade.isViewing());
        Assertions.assertTrue(facade.isPlaying());    }

    @Test
    public void enterGameAsBlack() {
        createAndJoin();
        facade.loginUser(user3.get("username"), user3.get("password"));
        List<GameData> gameList = facade.listGames();
        GameData game = gameList.getFirst();
        Assertions.assertEquals(game, facade.enterGame("game"));
        Assertions.assertTrue(facade.isViewing());
        Assertions.assertTrue(facade.isPlaying());    }

    @Test
    public void observeGameFailedNoUser() {
        createAndJoin();
        Assertions.assertNull(facade.enterGame("game"));
    }

    @Test
    public void listGames() {
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        List<String> gameNames = new ArrayList<>();
        for (int i=0; i<20; i++) {
            String gameName = "game" + String.valueOf(i);
            gameNames.add(gameName);
        }
        List<GameData> gameList = Assertions.assertDoesNotThrow(() -> facade.listGames());
        Assertions.assertEquals(20, gameList.size());
        for (GameData game : gameList) {
            boolean found = false;
            for (String name : gameNames) {
                if (game.gameName().equals(name)) {
                    found = true;
                    break;
                }
            }
            Assertions.assertTrue(found);
        }
    }

    @Test
    public void listGamesFailed() {
        Assertions.assertNull(facade.listGames());
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        facade.createGame("game");
        facade.logoutUser();
        Assertions.assertNull(facade.listGames());
    }

    private void createAndJoin() {
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        facade.createGame("game");
        facade.joinGame("game", "white");
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        facade.joinGame("game", "black");
        facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email"));
        facade.logoutUser();
    }

}
