package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public class GameDataAccessSQL implements GameDataAccess {
    GameDataAccessSQL() throws DataAccessException {
        makeGameTable();
    }
    public void clearData() {}
    public void newGame(GameData gameData) {}
    public Collection<GameData> getGames() {return null;}
    public GameData getGame(int id) {return null;}
    public int nextId() {return 0;}
    public void joinGame(ChessGame.TeamColor color, String username, int gameId) throws DataAccessException {}

    static private void makeGameTable() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();) {
            var statement = "CREATE TABLE IF NOT EXISTS game ( " +
                    "id INT PRIMARY KEY NOT NULL," +
                    "whiteusername VARCHAR(100)," +
                    "blackusername VARCHAR(100)," +
                    "gamename VARCHAR(100) UNIQUE NOT NULL," +
                    "game JSON NOT NULL)";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            throw(new DataAccessException("Unable to connect to the database"));
        }
    }
}
