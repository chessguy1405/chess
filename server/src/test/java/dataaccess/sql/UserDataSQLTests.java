package dataaccess.sql;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.*;

public class UserDataSQLTests {

    UserDataAccess userAccess = new UserDataAccessSQL();
    UserData user = new UserData("username", "password", "user@example.com");

    @BeforeAll
    static void init() {
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.makeUserTable();
        }
        catch(DataAccessException e) {
            throw(new RuntimeException("Failed to initialize database"));
        }
    }

    @BeforeEach
    void cleanup() {
        try {
            userAccess.clearData();
        }
        catch (DataAccessException e) {
            throw(new RuntimeException(e.getMessage()));
        }
    }


    @Test
    public void clearDataTest() {
        Assertions.assertDoesNotThrow(()->userAccess.clearData());
        for (int i = 0; i < 20; i++) {
            String username = String.valueOf(i);
            UserData exampleUser = new UserData(username, "password", username + "@example.com");
            Assertions.assertDoesNotThrow(()->userAccess.newUser(exampleUser));
        }
        Assertions.assertDoesNotThrow(()->userAccess.clearData());
        for (int i = 0; i < 20; i++) {
            String username = String.valueOf(i);
            UserData exampleUser = new UserData(username, "password", username + "@example.com");
            var returnedUser = Assertions.assertDoesNotThrow(()->userAccess.getUser(username));
            Assertions.assertNull(returnedUser);
        }
    }

    @Test
    public void newUserTest() {
        Assertions.assertDoesNotThrow(()->userAccess.newUser(user));
        var returnedUser = Assertions.assertDoesNotThrow(()->userAccess.getUser("username"));
        Assertions.assertEquals(user, returnedUser);
    }

    @Test
    public void getUserNonexistent() {
        var returnedUser = Assertions.assertDoesNotThrow(()->userAccess.getUser("username"));
        Assertions.assertNull(returnedUser);
    }
}
