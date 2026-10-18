package service;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import model.*;
import dataaccess.*;

import java.util.Collection;

public class ListGameServiceTests {
    GameDataAccess gamesAccess = new GameDataAccessMem();


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
        Collection<GameData> gamesList = Assertions.assertDoesNotThrow(() -> gamesAccess.getGames());
        assert (gamesList.size() == expected);
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
