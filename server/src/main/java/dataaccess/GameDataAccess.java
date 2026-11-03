package dataaccess;

import model.GameData;
import chess.ChessGame;
import java.util.Collection;

public interface GameDataAccess {
    void clearData() throws DataAccessException;
    void newGame(GameData gameData) throws DataAccessException;
    Collection<GameData> getGames() throws DataAccessException;
    GameData getGame(int id) throws DataAccessException;
    int nextId();
    void joinGame(ChessGame.TeamColor color, String username, int gameId) throws DataAccessException;
}
