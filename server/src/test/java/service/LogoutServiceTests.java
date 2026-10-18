package service;

import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.*;
import model.*;
import dataaccess.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogoutServiceTests {
    UserDataAccess usersAccess;
    AuthDataAccess authAccess;

    UserData user1 = new UserData("test", "test", "test");

    @BeforeAll
    public void init() {
        usersAccess.newUser(user1);
        authAccess.newAuth("test");
    }

    @BeforeEach
    public void loginTestUser() {
        if (authAccess.getAuth("test") == null) {
            authAccess.newAuth("test");
        }
    }

    @Test
    public void testTokenDoesNotExist() {
        LogoutRequest request = new LogoutRequest("not a token");
        Assertions.assertThrows(DoesNotExistException.class, () -> LogoutService.logout(request));
        assert(authAccess.getAuth("test") != null);
    }

    @Test
    public void testUserLoggedIn() {
        String authToken = authAccess.getAuth("test").authToken();
        LogoutRequest request = new LogoutRequest(authToken);
        Assertions.assertDoesNotThrow(() -> LogoutService.logout(request));
        assert(authAccess.getAuth("test") == null);
    }
}
