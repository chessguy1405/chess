package dataaccess;

import model.GameData;
import chess.ChessGame;
import java.util.Collection;

public interface GameDataAccess {
    void clearData();
    void newGame(GameData gameData);
    Collection<GameData> getGames();
    GameData getGame(int id);
    int nextId();
    void joinGame(ChessGame.TeamColor color, String username, int gameId) throws DataAccessException;
}
