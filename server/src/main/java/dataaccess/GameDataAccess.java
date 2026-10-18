package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDataAccess {
    void clearData();
    void newGame(GameData gameData);
    Collection<GameData> getGames();
    GameData getGame(int id);
}
