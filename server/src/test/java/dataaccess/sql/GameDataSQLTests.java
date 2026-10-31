package dataaccess.sql;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.*;

public class GameDataSQLTests {

    GameDataAccess gameAccess = new GameDataAccessSQL();
    UserDataAccess userAccess = new UserDataAccessSQL();
    UserData testUser1 = new UserData("testuser1", "testpassword", "user@example.com");
    UserData testUser2 = new UserData("testuser2", "testpassword", "user@example.com");
    int gameId = gameAccess.nextId();
    ChessGame game = new ChessGame();
    GameData testGame = new GameData(gameId, null, null, "testgame1", game);
    GameData whiteGame = new GameData(gameId, "testuser1", null, "testgame1", game);
    GameData blackGame = new GameData(gameId, null, "testuser2", "testgame", game);

    @BeforeAll
    static void init() {
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.makeUserTable();
            DatabaseManager.makeGameTable();
        }
        catch(DataAccessException e) {
            throw(new RuntimeException("Failed to initialize database"));
        }
    }

    @BeforeEach
    void cleanup() {
        gameAccess.clearData();
        if (userAccess.getUser("testuser1") == null) {
            userAccess.newUser(testUser1);
        }
        if (userAccess.getUser("testuser2") == null) {
            userAccess.newUser(testUser2);
        }
    }

    @Test
    public void clearDataTest() {
        Assertions.assertDoesNotThrow(()->gameAccess.clearData());
        for (int i = 0; i < 20; i++) {
            GameData exampleGame = new GameData(gameId + i, null, null, "game", new ChessGame());
            Assertions.assertDoesNotThrow(()->gameAccess.newGame(exampleGame));
        }
        Assertions.assertDoesNotThrow(()->gameAccess.clearData());
        for (int i = 0; i < 20; i++) {
            var searchId = gameId + i;
            var returnedGame = Assertions.assertDoesNotThrow(()->gameAccess.getGame(searchId));
            Assertions.assertNull(returnedGame);
        }
    }

    @Test
    public void newGameTest() {
        Assertions.assertDoesNotThrow(()->gameAccess. newGame(testGame));
        var returnedGame = Assertions.assertDoesNotThrow(()->gameAccess.getGame(gameId));
        Assertions.assertEquals(testGame, returnedGame);
    }

    @Test
    public void newGameUsedId() {
        Assertions.assertDoesNotThrow(()->gameAccess.newGame(testGame));
        var returnedGame = Assertions.assertDoesNotThrow(()->gameAccess.getGame(gameId));
        Assertions.assertEquals(testGame, returnedGame);
        Assertions.assertDoesNotThrow(()->gameAccess.newGame(whiteGame));
        returnedGame = Assertions.assertDoesNotThrow(()->gameAccess.getGame(gameId));
        Assertions.assertEquals(testGame, returnedGame);
    }

    @Test
    public void getNonexistentGame() {
        var returnedGame = Assertions.assertDoesNotThrow(()->gameAccess.getGame(gameId));
        Assertions.assertNull(returnedGame);
    }

    @Test
    public void nextIDTest() {
        var exampleId = Assertions.assertDoesNotThrow(()->gameAccess.nextId());
        Assertions.assertInstanceOf(Integer.class, exampleId);
    }

    @Test
    public void joinGameAsWhite() {
        gameAccess.newGame(testGame);
        var returnedGame = gameAccess.getGame(gameId);
        Assertions.assertEquals(testGame, returnedGame);
        Assertions.assertDoesNotThrow(()->gameAccess.joinGame(ChessGame.TeamColor.WHITE, "testuser1", gameId));
        returnedGame = gameAccess.getGame(gameId);
        Assertions.assertEquals(whiteGame, returnedGame);
    }

    @Test
    public void joinGameAsBlack() {
        gameAccess.newGame(testGame);
        var returnedGame = gameAccess.getGame(gameId);
        Assertions.assertEquals(testGame, returnedGame);
        Assertions.assertDoesNotThrow(()->gameAccess.joinGame(ChessGame.TeamColor.BLACK, "testuser2", gameId));
        returnedGame = gameAccess.getGame(gameId);
        Assertions.assertEquals(blackGame, returnedGame);
    }

    @Test
    public void joinGameAsWhiteFail() {
        gameAccess.newGame(whiteGame);
        var returnedGame = gameAccess.getGame(gameId);
        Assertions.assertEquals(whiteGame, returnedGame);
        Assertions.assertThrows(DataAccessException.class, ()->gameAccess.joinGame(ChessGame.TeamColor.WHITE, "testuser2", gameId));
        returnedGame = gameAccess.getGame(gameId);
        Assertions.assertEquals(whiteGame, returnedGame);
    }

    @Test
    public void joinGameAsBlackFail() {
        gameAccess.newGame(blackGame);
        var returnedGame = gameAccess.getGame(gameId);
        Assertions.assertEquals(blackGame, returnedGame);
        Assertions.assertThrows(DataAccessException.class, ()->gameAccess.joinGame(ChessGame.TeamColor.BLACK, "testuser1", gameId));
        returnedGame = gameAccess.getGame(gameId);
        Assertions.assertEquals(blackGame, returnedGame);
    }

}
