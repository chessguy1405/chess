package service;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import model.*;
import dataaccess.*;

import java.util.Collection;
import dataaccess.sql.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ListGameServiceTests {
    GameDataAccess gamesAccess = new GameDataAccessSQL();
    UserDataAccess usersAccess = new UserDataAccessSQL();
    AuthDataAccess authAccess = new AuthDataAccessSQL();
    AuthData auth1;

    @BeforeAll
    public void init() {
        try {
            UserData user1 = new UserData("test", "test", "test");
            if (usersAccess.getUser("test") == null) {
                usersAccess.newUser(user1);
            }
            auth1 = authAccess.newAuth("test");
        }
        catch (DataAccessException e) {
            throw(new RuntimeException(e.getMessage()));
        }
    }

    @BeforeEach
    public void cleanup() {
        try {
            gamesAccess.clearData();
        }
        catch (DataAccessException e) {
            throw(new RuntimeException(e.getMessage()));
        }
    }

    @Test
    public void testListWhenEmpty() {
        checkSize(0);
    }

    @Test
    public void testListWithOne() {
        addGames(1);
        checkSize(1);
    }

    @Test
    public void testListWithTwenty() {
        addGames(20);
        checkSize(20);
    }

    private void checkSize(int expected) {
        ListGameRequest request = new ListGameRequest(auth1.authToken());
        ListGameResult games = Assertions.assertDoesNotThrow(() -> ListGameService.listGames(request));
        assert (games.games().size() == expected);
    }

    private void addGames(int gameCount) {
        int i = 0;
        while (i < gameCount) {
            GameData game = new GameData(i, "test", "test", "test" + String.valueOf(i), new ChessGame());
            Assertions.assertDoesNotThrow(()->gamesAccess.newGame(game));
            i++;
        }
    }
}
