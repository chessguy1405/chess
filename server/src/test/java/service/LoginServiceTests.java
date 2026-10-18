package service;

import org.junit.jupiter.api.*;
import model.*;
import dataaccess.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginServiceTests {
    UserDataAccess usersAccess;
    AuthDataAccess authAccess;

    UserData user1 = new UserData("test", "test", "test");
    AuthData auth1;

    @BeforeAll
    public void init() {
        usersAccess.newUser(user1);
        auth1 = authAccess.getAuth("test");
        authAccess.removeAuth(auth1);
    }

    @BeforeEach
    public void logoutTestUser() {
        auth1 = authAccess.getAuth("test");
        if (auth1 != null) {
            authAccess.removeAuth(auth1);
        }
    }

    @Test
    public void testUserNotExist() {
        LoginRequest request = new LoginRequest("exam", "test");
        Assertions.assertThrows(DoesNotExistException.class, () -> LoginService.login(request));
        assert(authAccess.getAuth("test") == null);
    }

    @Test
    public void testWrongPassword() {
        LoginRequest request = new LoginRequest("test", "exam");
        Assertions.assertThrows(BadPasswordException.class, () -> LoginService.login(request));
        assert(authAccess.getAuth("test") == null);
    }

    @Test
    public void testValidLogin() {
        LoginRequest request = new LoginRequest("test", "test");
        Assertions.assertDoesNotThrow(() -> LoginService.login(request));
        assert(authAccess.getAuth("test") != null);
    }

    @Test
    public void testAlreadyLoggedIn() {
        LoginRequest request = new LoginRequest("test", "test");
        LoginResult auth2 = Assertions.assertDoesNotThrow(() -> LoginService.login(request));
        LoginResult auth3 = Assertions.assertDoesNotThrow(() -> LoginService.login(request));
        assert(authAccess.getAuth("test") != null);
        assert(auth2.equals(auth3));
    }
}
