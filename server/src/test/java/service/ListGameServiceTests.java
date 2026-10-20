package service;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import model.*;
import dataaccess.*;

import java.util.Collection;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ListGameServiceTests {
    GameDataAccess gamesAccess = new GameDataAccessMem();
    UserDataAccess usersAccess = new UserDataAccessMem();
    AuthDataAccess authAccess = new AuthDataAccessMem();
    AuthData auth1;

    @BeforeAll
    public void init() {
        UserData user1 = new UserData("test", "test", "test");
        usersAccess.newUser(user1);
        auth1 = authAccess.newAuth("test");
    }

    @BeforeEach
    public void cleanup() {
        gamesAccess.clearData();
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
            gamesAccess.newGame(game);
            i++;
        }
    }
}
