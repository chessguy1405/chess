package service;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.*;
import dataaccess.*;

import java.util.Collection;

public class ClearDataServiceTests {

    GameDataAccess gamesAccess = new GameDataAccessMem();
    UserDataAccess usersAccess = new UserDataAccessMem();


    @BeforeEach
    public void cleanupData() {
        try {
            gamesAccess.clearData();
            usersAccess.clearData();
        }
        catch (DataAccessException e) {
            throw(new RuntimeException(e.getMessage()));
        }
    }

    @Test
    public void clearWhenEmpty() {
        checkUsersAndGames();
    }

    @Test
    public void clearWithOneGame() {
        addGames(1);
        checkUsersAndGames();
    }

    @Test
    public void clearWithFiveGames() {
        addGames(5);
        checkUsersAndGames();
    }

    @Test
    public void clearWithOneUser() {
        addUsers(1);
        checkUsersAndGames();
    }

    @Test
    public void clearWithFiveUsers(){
        addUsers(5);
        checkUsersAndGames();
    }

    @Test
    public void clearWithOneGameOneUser() {
        addGames(1);
        addUsers(1);
        checkUsersAndGames();
    }

    @Test
    public void clearWithFiveGamesOneUser() {
        addGames(5);
        addUsers(1);
        checkUsersAndGames();
    }

    @Test
    public void clearWithOneGameFiveUsers() {
        addGames(1);
        addUsers(5);
        checkUsersAndGames();
    }

    @Test
    public void clearWithFiveGamesFiveUsers() {
        addGames(5);
        addUsers(5);
        checkUsersAndGames();
    }

    private void checkUsersAndGames() {
        Assertions.assertDoesNotThrow(ClearDataService::clearAllData);
        Collection<GameData> gameList = Assertions.assertDoesNotThrow(() -> gamesAccess.getGames());
        Collection<UserData> userList = Assertions.assertDoesNotThrow(() -> usersAccess.getUsers());
        Assertions.assertNull(gameList);
        Assertions.assertNull(userList);
    }

    private void addGames(int gameCount) {
        int i = 0;
        while (i < gameCount) {
            String gameName = String.valueOf(i);
            final int j = i; // Literally this is just to make the runtime happy since it can't make me happy
            Assertions.assertDoesNotThrow(()->gamesAccess.newGame(new GameData(j, "test", "test", gameName, new ChessGame())));
            i++;
        }
    }

    private void addUsers(int userCount) {
        int i = 0;
        while (i < userCount) {
            String userName = String.valueOf(i);
            Assertions.assertDoesNotThrow(() -> usersAccess.newUser(new UserData(userName, userName, userName)));
            i++;
        }
    }
}
