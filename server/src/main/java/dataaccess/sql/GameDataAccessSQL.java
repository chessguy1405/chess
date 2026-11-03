package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDataAccess;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public class GameDataAccessSQL implements GameDataAccess {

    public void clearData() {
        String sql = "DELETE FROM game";
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement(sql);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw(new DataAccessException("Unable to access database"));
        }
    }
    public void newGame(GameData gameData) {}
    public Collection<GameData> getGames() {return null;}
    public GameData getGame(int id) {return null;}
    public int nextId() {return 0;}
    public void joinGame(ChessGame.TeamColor color, String username, int gameId) throws DataAccessException {}


}
