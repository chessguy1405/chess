package service;

import dataaccess.*;
import org.junit.jupiter.api.*;
import model.*;

import java.util.Collection;
import dataaccess.sql.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateGameServiceTests {

    GameDataAccess gamesAccess = new GameDataAccessSQL();
    AuthDataAccess authAccess = new AuthDataAccessSQL();
    UserDataAccess userAccess = new UserDataAccessSQL();
    UserData user = new UserData("test", "test", "test");
    AuthData auth;

    @BeforeAll
    public void init() {
        try {
            if (userAccess.getUser("test") == null) {
                userAccess.newUser(user);
            }
            auth = authAccess.newAuth("test");
        }
        catch (DataAccessException e) {
            throw(new RuntimeException(e.getMessage()));
        }
    }

    @BeforeEach
    public void clearGames() {
        try {
            gamesAccess.clearData();
        }
        catch (DataAccessException e) {
            throw(new RuntimeException(e.getMessage()));
        }
    }

    @Test
    public void testAddWhenEmpty() {
        Assertions.assertDoesNotThrow(() -> CreateGameService.newGame(makeGame(1)));
        countGames(1);
    }

    @Test
    public void testAddTwenty() {
        for (int i = 0; i < 20; i++) {
            final int id = i;
            Assertions.assertDoesNotThrow(() -> CreateGameService.newGame(makeGame(id)));
            countGames(i+1);
        }
    }

    private NewGameRequest makeGame(int id) {
        return new NewGameRequest(auth.authToken(), "test" + String.valueOf(id));
    }

    private void countGames(int expected) {
        Collection<GameData> gameList = Assertions.assertDoesNotThrow(()->gamesAccess.getGames());
        Assertions.assertEquals(expected, gameList.size());
    }
}
