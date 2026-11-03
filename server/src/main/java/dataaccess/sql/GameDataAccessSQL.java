package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDataAccess;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public class GameDataAccessSQL implements GameDataAccess {
    private static int gameId = 1;
    private final Gson serializer = new Gson();

    public void clearData() throws DataAccessException {
        String sql = "DELETE FROM game";
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement(sql);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw(new DataAccessException("Unable to access database"));
        }
    }
    public void newGame(GameData gameData) throws DataAccessException {
        String sql = "INSERT INTO game(id, whiteusername, blackusername, gamename, game) VALUES (?, ?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement(sql);
            statement.setInt(1, gameData.gameID());
            statement.setString(2, gameData.whiteUsername());
            statement.setString(3, gameData.blackUsername());
            statement.setString(4, gameData.gameName());
            var gameJson = serializer.toJson(gameData.game());
            statement.setString(5, gameJson);
            statement.executeUpdate();
        }
        catch(SQLException e) {
            throw(new DataAccessException(e.getMessage()));
        }
    }
    public Collection<GameData> getGames() {return null;}
    public GameData getGame(int id) {return null;}
    public int nextId() {return gameId;}
    public void joinGame(ChessGame.TeamColor color, String username, int gameId) throws DataAccessException {}


}
