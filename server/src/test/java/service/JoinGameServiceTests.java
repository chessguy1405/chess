package service;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import model.*;
import dataaccess.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JoinGameServiceTests {

    GameDataAccess gamesAccess;

    AuthData auth1;
    AuthData auth2;
    AuthData auth3;

    @BeforeAll
    public void init() {
        gamesAccess = new GameDataAccessMem();
        UserDataAccess usersAccess = new UserDataAccessMem();
        AuthDataAccess authAccess = new AuthDataAccessMem();
        UserData user1 = new UserData("test", "test", "test");
        UserData user2 = new UserData("exam", "exam", "exam");
        UserData user3 = new UserData("quiz", "quiz", "quiz");
        GameData game1 = new GameData(0, null, null, "Game 1", new ChessGame());
        GameData game2 = new GameData(1, "test", "quiz", "Game 2", new ChessGame());
        GameData game3 = new GameData(2, "test", "exam", "Game 3", new ChessGame());

        usersAccess.newUser(user1);
        usersAccess.newUser(user2);
        usersAccess.newUser(user3);

        gamesAccess.newGame(game1);
        gamesAccess.newGame(game2);
        gamesAccess.newGame(game3);

        auth1 = authAccess.newAuth(user1.username());
        auth2 = authAccess.newAuth(user2.username());
        auth3 = authAccess.newAuth(user3.username());
    }

    @Test
    public void testJoinWhenEmpty() {
        JoinGameRequest request1 = new JoinGameRequest(auth2.authToken(), ChessGame.TeamColor.WHITE, 0);
        JoinGameRequest request2 = new JoinGameRequest(auth3.authToken(), ChessGame.TeamColor.BLACK, 0);
        Assertions.assertDoesNotThrow(() -> JoinGameService.joinGame(request1));
        Assertions.assertDoesNotThrow(() -> JoinGameService.joinGame(request2));
        GameData game = gamesAccess.getGame(0);
        Assertions.assertEquals("exam", game.whiteUsername());
        Assertions.assertEquals("quiz", game.blackUsername());
    }

    @Test
    public void testJoinWhenTaken() {
        JoinGameRequest request1 = new JoinGameRequest(auth3.authToken(), ChessGame.TeamColor.WHITE, 1);
        JoinGameRequest request2 = new JoinGameRequest(auth2.authToken(), ChessGame.TeamColor.BLACK, 1);
        Assertions.assertThrows(TeamTakenException.class, () -> JoinGameService.joinGame(request1));
        Assertions.assertThrows(TeamTakenException.class, () -> JoinGameService.joinGame(request2));
        GameData game = gamesAccess.getGame(1);
        assert(game.whiteUsername().equals("test"));
        assert(game.blackUsername().equals("quiz"));
    }

    @Test
    public void testJoinWhenOwn() {
        JoinGameRequest request1 = new JoinGameRequest(auth1.authToken(), ChessGame.TeamColor.WHITE, 2);
        JoinGameRequest request2 = new JoinGameRequest(auth2.authToken(), ChessGame.TeamColor.BLACK, 2);
        Assertions.assertThrows(TeamTakenException.class, () -> JoinGameService.joinGame(request1));
        Assertions.assertThrows(TeamTakenException.class, () -> JoinGameService.joinGame(request2));
        GameData game = gamesAccess.getGame(2);
        assert(game.whiteUsername().equals("test"));
        assert(game.blackUsername().equals("exam"));
    }
}
