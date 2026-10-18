package service;

import dataaccess.UserDataAccess;
import dataaccess.UserDataAccessMem;
import org.junit.jupiter.api.*;
import model.UserData;
import model.LoginResult;

public class RegisterServiceTests {

    UserDataAccess cleanup = new UserDataAccessMem();

    UserData user1 = new UserData("user", "pass", "email");
    UserData user2 = new UserData("user", "pass", "email");
    UserData user3 = new UserData("username", "password", "address");

    @BeforeEach
    public void cleanupUsers() {
        cleanup.clearData();
    }

    @Test
    public void testNewUserWhenEmpty() {
        LoginResult login = Assertions.assertDoesNotThrow(() -> RegisterService.register(user1));
        Assertions.assertEquals("user", login.username());
    }

    @Test
    public void testNewUserWhenNotEmpty() {
        LoginResult login1 = RegisterService.register(user1);
        LoginResult login2 = Assertions.assertDoesNotThrow(() -> RegisterService.register(user3));
        Assertions.assertEquals("username", login2.username());
    }

    @Test
    public void testExistingUserSameRecord() {
        LoginResult login1 = RegisterService.register(user1);
        Assertions.assertThrows(UserExistsException.class, () -> RegisterService.register(user1));
    }

    @Test
    public void testExistingUserNewRecord() {
        LoginResult login1 = RegisterService.register(user1);
        Assertions.assertThrows(UserExistsException.class, () -> RegisterService.register(user2));
    }
}
