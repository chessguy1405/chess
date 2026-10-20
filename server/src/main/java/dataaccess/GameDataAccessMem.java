package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameDataAccessMem implements GameDataAccess {
   private static Map<Integer, GameData> games = new HashMap<>();
   private static int maxId = 0;

    public void clearData() {
        games = new HashMap<>();
    }

    public void newGame(GameData gameData) {
        games.put(gameData.gameID(), gameData);
        maxId++;
    }

    public Collection<GameData> getGames() {
        if (games.isEmpty()) { return null; }
        return(games.values());
    }

    public GameData getGame(int id) {
        return(games.get(id));
    }

    public int nextId() {
        return(maxId);
    }
}
