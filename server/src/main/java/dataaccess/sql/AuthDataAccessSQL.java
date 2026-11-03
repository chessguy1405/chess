package dataaccess.sql;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthDataAccessSQL implements AuthDataAccess {

    public AuthData newAuth(String username) throws DataAccessException {
        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, username);
        String user_sql = "SELECT * FROM user WHERE username=?";
        String auth_sql = "INSERT INTO auth(authtoken, username) VALUES(?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            var find_user = conn.prepareStatement(user_sql);
            find_user.setString(1, username);
            ResultSet result = find_user.executeQuery();
            if (result.next()) {
                var statement = conn.prepareStatement(auth_sql);
                statement.setString(1, token);
                statement.setString(2, username);
                statement.executeUpdate();
                return authData;
            }
            return null;
        }
        catch (SQLException e) {
            throw(new DataAccessException("Unable to add auth data"));
        }
    }
    public AuthData getAuth(String checkString) throws DataAccessException {
        String sql = "SELECT * FROM auth WHERE authtoken=? OR username=?";
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement(sql);
            statement.setString(1, checkString);
            statement.setString(2, checkString);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String authToken = result.getString("authtoken");
                String userName = result.getString("username");
                return new AuthData(authToken, userName);
            }
            return null;
        }
        catch(SQLException e) {
            throw(new DataAccessException("Unable to get auth data"));
        }
    }
    public void removeAuth(AuthData authData) {}
    public void clearData() throws DataAccessException {
        String sql = "DELETE FROM auth";
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement(sql);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw(new DataAccessException("Unable to access database"));
        }
    }


}
