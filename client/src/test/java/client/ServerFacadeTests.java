package client;

import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;

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
        facade = new ServerFacade();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
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
        Assertions.assertTrue(facade.registerUser(user2.get("username"), user2.get("password"), user2.get("email")));
        Assertions.assertTrue(facade.registerUser(user3.get("username"), user3.get("password"), user3.get("email")));
    }

    @Test
    public void registerFailedTest() {
        Assertions.assertTrue(facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email")));
        Assertions.assertFalse(facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email")));
    }

    @Test
    public void logoutTest() {
        Assertions.assertTrue(facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email")));
        Assertions.assertTrue(facade.isLoggedIn());
        Assertions.assertEquals("test", facade.getCurrentUser());
        Assertions.assertTrue(facade.logoutUser());
        Assertions.assertFalse(facade.isLoggedIn());
        Assertions.assertNull(facade.getCurrentUser());
    }

    @Test
    public void logoutFailedNoUser() {
        Assertions.assertFalse(facade.logoutUser());
        Assertions.assertTrue(facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email")));
        Assertions.assertTrue(facade.isLoggedIn());
        Assertions.assertEquals("test", facade.getCurrentUser());
        Assertions.assertTrue(facade.logoutUser());
        Assertions.assertFalse(facade.isLoggedIn());
        Assertions.assertNull(facade.getCurrentUser());
        Assertions.assertFalse(facade.logoutUser());
    }

    @Test
    public void loginTest() {
        Assertions.assertTrue(facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email")));
        Assertions.assertTrue(facade.logoutUser());
        Assertions.assertTrue(facade.loginUser(user1.get("username"), user1.get("password")));
        Assertions.assertEquals("test", facade.getCurrentUser());
    }

    @Test
    public void loginFailedWrongPassword() {
        Assertions.assertTrue(facade.registerUser(user1.get("username"), user1.get("password"), user1.get("email")));
        Assertions.assertTrue(facade.logoutUser());
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
    public void joinGame() {}

    @Test
    public void joinGameFailedNoUser() {}

    @Test
    public void joinGameFailedColorTaken() {}

    @Test
    public void observeGame() {}

    @Test
    public void observeGameAsWhite() {}

    @Test
    public void observeGameAsBlack() {}

}
