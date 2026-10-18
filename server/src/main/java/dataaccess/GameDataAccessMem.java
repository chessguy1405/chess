package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameDataAccessMem implements GameDataAccess {
   static Map<Integer, GameData> games = new HashMap<>();

    public void clearData() {
        games = new HashMap<>();
    }

    public void newGame(GameData gameData) {
        games.put(gameData.gameID(), gameData);
    }

    public Collection<GameData> getGames() {
        if (games.isEmpty()) { return null; }
        return(games.values());
    }

    public GameData getGame(int id) {
        return(games.get(id));
    }
}
