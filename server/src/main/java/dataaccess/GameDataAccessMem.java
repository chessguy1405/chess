package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameDataAccessMem implements GameDataAccess {
   Map<Integer, GameData> games;

   public GameDataAccessMem() {
       games = new HashMap<>();
   }

    public void clearData() {
        // Clear all game data from in-memory storage
    }

    public void newGame(GameData gameData) {
        // Create a new game
    }

    public Collection<GameData> getGames() {
        // Get a list of all games
        return null;
    }
}
