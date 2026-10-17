package service;

import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.*;
import dataaccess.*;

import java.util.Collection;

public class ClearDataServiceTests {

    ClearGameData cleanupGames = new ClearGameDataMem();
    ClearUserData cleanupUsers = new ClearUserDataMem();
    ReadGameData gameReader = new ReadGameDataMem();
    ReadUserData userReader = new ReadUserDataMem();
    CreateGameData gameMaker = new CreateGameDataMem();
    CreateUserData userMaker = new CreateUserDataMem();

    @BeforeEach
    public void cleanupData() {
        cleanupGames.clearData();
        cleanupUsers.clearData();
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
        Collection<GameData> gameList = Assertions.assertDoesNotThrow(() -> gameReader.getGames());
        Collection<UserData> userList = Assertions.assertDoesNotThrow(() -> userReader.getUsers());
        assert(gameList == null);
        assert(userList == null);
    }

    private void addGames(int gameCount) {
        int i = 0;
        while (i < gameCount) {
            String gameName = String.valueOf(i);
            gameMaker.newGame(new GameData(i, "test", "test", gameName, new ChessGame()));
            i++;
        }
    }

    private void addUsers(int userCount) {
        int i = 0;
        while (i < userCount) {
            String userName = String.valueOf(i);
            userMaker.newUser(new UserData(userName, userName, userName));
            i++;
        }
    }
}
