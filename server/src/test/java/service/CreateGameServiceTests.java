package service;

import dataaccess.*;
import org.junit.jupiter.api.*;
import model.*;

import java.util.Collection;

public class CreateGameServiceTests {

    GameDataAccess gamesAccess = new GameDataAccessMem();

    @BeforeEach
    public void clearGames() {
        gamesAccess.clearData();
    }

    @Test
    public void testAddWhenEmpty() {
        Assertions.assertDoesNotThrow(() -> CreateGameService.newGame(makeGame()));
        countGames(1);
    }

    @Test
    public void testAddTwenty() {
        for (int i = 0; i < 20; i++) {
            final int id = i;
            Assertions.assertDoesNotThrow(() -> CreateGameService.newGame(makeGame()));
            countGames(i+1);
        }
    }

    private NewGameRequest makeGame() {
        return new NewGameRequest("test_token", "test");
    }

    private void countGames(int expected) {
        Collection<GameData> gameList = gamesAccess.getGames();
        Assertions.assertEquals(expected, gameList.size());
    }
}
