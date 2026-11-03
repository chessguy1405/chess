package service;

import org.junit.jupiter.api.*;
import model.*;
import dataaccess.*;

import java.sql.SQLException;
import dataaccess.sql.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginServiceTests {
    UserDataAccess usersAccess = new UserDataAccessSQL();
    AuthDataAccess authAccess = new AuthDataAccessSQL();

    UserData user1 = new UserData("test", "test", "test");
    AuthData auth1;

    @BeforeAll
    public void init() {
        try {
            usersAccess.clearData();
            RegisterService.register(user1);
        }
        catch (DataAccessException e) {
            throw(new RuntimeException(e.getMessage()));
        }
    }

    @BeforeEach
    public void logoutTestUser() {
        try {
            auth1 = authAccess.getAuth("test");
            if (auth1 != null) {
                authAccess.removeAuth(auth1);
            }
        }
        catch (DataAccessException e) {
            throw(new RuntimeException(e.getMessage()));
        }    }

    @Test
    public void testUserNotExist() {
        LoginRequest request = new LoginRequest("exam", "test");
        Assertions.assertThrows(DoesNotExistException.class, () -> LoginService.login(request));
        var result = Assertions.assertDoesNotThrow(() -> authAccess.getAuth("test"));
        Assertions.assertNull(result);
    }

    @Test
    public void testWrongPassword() {
        Assertions.assertDoesNotThrow(()->authAccess.clearData());
        LoginRequest request = new LoginRequest("test", "exam");
        Assertions.assertThrows(BadPasswordException.class, () -> LoginService.login(request));
        var result = Assertions.assertDoesNotThrow(() -> authAccess.getAuth("test"));
        Assertions.assertNull(result);
    }

    @Test
    public void testValidLogin() {
        LoginRequest request = new LoginRequest("test", "test");
        Assertions.assertDoesNotThrow(() -> LoginService.login(request));
        var result = Assertions.assertDoesNotThrow(() -> authAccess.getAuth("test"));
        Assertions.assertNotNull(result);
    }

    @Test
    public void testAlreadyLoggedIn() {
        LoginRequest request = new LoginRequest("test", "test");
        LoginResult auth2 = Assertions.assertDoesNotThrow(() -> LoginService.login(request));
        LoginResult auth3 = Assertions.assertDoesNotThrow(() -> LoginService.login(request));
        var result = Assertions.assertDoesNotThrow(() -> authAccess.getAuth("test"));
        Assertions.assertNotNull(result);
        Assertions.assertNotEquals(auth2.authToken(), auth3.authToken());
    }
}
