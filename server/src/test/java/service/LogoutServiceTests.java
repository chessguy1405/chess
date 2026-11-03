package service;

import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.*;
import model.*;
import dataaccess.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogoutServiceTests {
    UserDataAccess usersAccess = new UserDataAccessMem();
    AuthDataAccess authAccess = new AuthDataAccessMem();

    UserData user1 = new UserData("test", "test", "test");

    @BeforeAll
    public void init() {
        try {
            usersAccess.newUser(user1);
            authAccess.newAuth("test");
        }
        catch (DataAccessException e) {
            throw(new RuntimeException(e.getMessage()));
        }
    }

    @BeforeEach
    public void loginTestUser() {
        try {
            if (authAccess.getAuth("test") == null) {
                authAccess.newAuth("test");
            }
        }
        catch (DataAccessException e) {
            throw(new RuntimeException(e.getMessage()));
        }
    }

    @Test
    public void testTokenDoesNotExist() {
        LogoutRequest request = new LogoutRequest("not a token");
        Assertions.assertThrows(DoesNotExistException.class, () -> LogoutService.logout(request));
        var result = Assertions.assertDoesNotThrow(() -> authAccess.getAuth("test"));
        Assertions.assertNull(result);
    }

    @Test
    public void testUserLoggedIn() {
        String authToken = Assertions.assertDoesNotThrow(()->authAccess.getAuth("test").authToken());
        LogoutRequest request = new LogoutRequest(authToken);
        Assertions.assertDoesNotThrow(() -> LogoutService.logout(request));
        var result = Assertions.assertDoesNotThrow(()->authAccess.getAuth("test"));
        Assertions.assertNotNull(result);
    }
}
