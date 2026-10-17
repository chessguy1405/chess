package service;

import dataaccess.ClearUserData;
import dataaccess.ClearUserDataMem;
import org.junit.jupiter.api.*;
import model.UserData;
import model.LoginResult;

public class RegisterServiceTests {

    ClearUserData cleanup = new ClearUserDataMem();

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
        assert(login.username().equals("user"));
    }

    @Test
    public void testNewUserWhenNotEmpty() {
        LoginResult login1 = RegisterService.register(user1);
        LoginResult login2 = Assertions.assertDoesNotThrow(() -> RegisterService.register(user3));
        assert(login2.username().equals("username"));
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
