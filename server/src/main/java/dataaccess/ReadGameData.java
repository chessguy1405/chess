package dataaccess;

import model.GameData;

import java.util.Collection;

public interface ReadGameData {
    Collection<GameData> getGames();
}
