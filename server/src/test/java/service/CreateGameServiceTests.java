package service;

import chess.ChessGame;
import dataaccess.*;
import org.junit.jupiter.api.*;
import model.*;

import java.util.Collection;

public class CreateGameServiceTests {

    ClearGameData cleanup = new ClearGameDataMem();
    ReadGameData gameReader = new ReadGameDataMem();

    @BeforeEach
    public void clearGames() {
        cleanup.clearData();
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
        Collection<GameData> gameList = gameReader.getGames();
        assert(gameList.size() == expected);
    }
}
