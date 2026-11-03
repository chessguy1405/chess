package dataaccess.sql;

import dataaccess.*;
import org.junit.jupiter.api.*;
import model.*;

public class AuthDataSQLTests {

    AuthDataAccess authAccess = new AuthDataAccessSQL();
    UserDataAccess userAccess = new UserDataAccessSQL();
    UserData testUser = new UserData("testuser1", "testpassword", "user@example.com");

    @BeforeAll
    static void init() {
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.makeUserTable();
            DatabaseManager.makeAuthTable();
        }
        catch(DataAccessException e) {
            throw(new RuntimeException("Failed to initialize database"));
        }
    }

    @BeforeEach
    void cleanup() {
        try {
            authAccess.clearData();
            if (userAccess.getUser("testuser1") == null) {
                userAccess.newUser(testUser);
            }
        }
        catch(DataAccessException e) {
            throw(new RuntimeException("Failed to connect to database"));
        }
    }

    @Test
    public void newAuthTest() {
        Assertions.assertDoesNotThrow(()->authAccess.newAuth("testuser1"));
        var returnedAuth = Assertions.assertDoesNotThrow(()->authAccess.getAuth("testuser1"));
        Assertions.assertNotNull(returnedAuth);
    }

    @Test
    public void newAuthNonexistent() {
        var returnedAuth = Assertions.assertDoesNotThrow(()->authAccess.newAuth("erftghyjuykjygtrfedws"));
        Assertions.assertNull(returnedAuth);
        returnedAuth = Assertions.assertDoesNotThrow(()->authAccess.getAuth("erftghyjuykjygtrfedws"));
        Assertions.assertNull(returnedAuth);
    }

    @Test
    public void getAuthNonexistent() {
        var returnedAuth = Assertions.assertDoesNotThrow(()->authAccess.getAuth("testuser1"));
        Assertions.assertNull(returnedAuth);
    }

    @Test
    public void removeDataTest() {
        Assertions.assertDoesNotThrow(()->authAccess.newAuth("testuser1"));
        final var returnedAuth = Assertions.assertDoesNotThrow(()->authAccess.getAuth("testuser1"));
        Assertions.assertNotNull(returnedAuth);
        Assertions.assertDoesNotThrow(()->authAccess.removeAuth(returnedAuth));
        final var newReturnedAuth = Assertions.assertDoesNotThrow(()->authAccess.getAuth("testuser1"));
        Assertions.assertNull(newReturnedAuth);
    }

    @Test
    public void clearDataTest() {
        Assertions.assertDoesNotThrow(()->authAccess.clearData());
        for(int i = 0; i < 20; i++) {
            Assertions.assertDoesNotThrow(() -> authAccess.newAuth("testuser1"));
        }
        Assertions.assertDoesNotThrow(()->authAccess.clearData());
        var returnedAuth = Assertions.assertDoesNotThrow(()->authAccess.getAuth("testuser1"));
        Assertions.assertNull(returnedAuth);
    }
}
