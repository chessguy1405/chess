package client;

import org.junit.jupiter.api.*;
import server.Server;
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
    public void logoutTest() {}

    @Test
    public void logoutFailedNoUser() {}

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
    public void loginFailedWrongUser() {}

    @Test
    public void newGame() {}

    @Test
    public void newGameFailed() {}

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
