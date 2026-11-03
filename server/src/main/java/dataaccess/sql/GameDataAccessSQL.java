package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;

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
            gameId++;
        }
        catch(SQLException e) {
            throw(new DataAccessException(e.getMessage()));
        }
    }

    public Collection<GameData> getGames() throws DataAccessException {
        String sql = "SELECT * FROM game";
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            Collection<GameData> gameList = new HashSet<>();
            while (result.next()) {
                gameList.add(constructGame(result));
            }
            if (gameList.isEmpty()) {
                return null;
            }
            return gameList;
        }
        catch (SQLException e) {
            throw(new DataAccessException(e.getMessage()));
        }
    }

    public GameData getGame(int id) throws DataAccessException {
        String sql = "SELECT * FROM game WHERE id=?";
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return constructGame(result);
            }
            return null;
        }
        catch (SQLException e) {
            throw(new DataAccessException(e.getMessage()));
        }
    }

    public int nextId() {return gameId;}

    public void joinGame(@NotNull ChessGame.TeamColor color, String username, int gameId) throws DataAccessException {
        String update_sql;
        String check_sql = "SELECT * FROM game WHERE id=?";
        String stringColor;

        if (color.equals(ChessGame.TeamColor.WHITE)) {
            update_sql = "UPDATE game SET whiteusername=? WHERE id=?";
            stringColor = "white";
        }
        else {
            update_sql = "UPDATE game SET blackusername=? WHERE id=?";
            stringColor = "black";
        }
        try (var conn = DatabaseManager.getConnection()) {
            var check = conn.prepareStatement(check_sql);
            check.setInt(1, gameId);
            ResultSet result = check.executeQuery();
            if (!result.next()) {
                throw new DataAccessException("Game does not exist");
            }
            boolean isTaken = stringColor.equals("black") && result.getString("blackusername") != null;
            if (stringColor.equals("white") && result.getString("whiteusername") != null) {
                isTaken = true;
            }
            if (!isTaken) {
                var statement = conn.prepareStatement(update_sql);
                statement.setString(1, username);
                statement.setInt(2, gameId);
                statement.executeUpdate();
            }
            else {
                throw(new DataAccessException(stringColor + " already taken"));
            }
        }
        catch (SQLException e) {
            throw(new DataAccessException(e.getMessage()));
        }
    }

    private GameData constructGame(ResultSet result) throws SQLException {
        int gameId = result.getInt("id");
        String whiteUsername = result.getString("whiteusername");
        String blackUsername = result.getString("blackusername");
        String gameName = result.getString("gamename");
        String gameJson = result.getString("game");
        ChessGame game = serializer.fromJson(gameJson, ChessGame.class);
        return new GameData(gameId, whiteUsername, blackUsername, gameName, game);
    }
}
