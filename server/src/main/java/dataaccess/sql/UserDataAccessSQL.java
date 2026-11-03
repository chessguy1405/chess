package dataaccess.sql;

import dataaccess.*;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class UserDataAccessSQL implements UserDataAccess {

    public void clearData() throws DataAccessException {
        String sql = "DELETE FROM user";
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement(sql);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw(new DataAccessException("Unable to access database"));
        }
    }
    public Collection<UserData> getUsers() throws DataAccessException {
        String sql = "SELECT * FROM user";
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            Collection<UserData> users = new HashSet<>();
            while (result.next()) {
                String name = result.getString("username");
                String email = result.getString("email");
                String password = result.getString("password");
                UserData user = new UserData(name, password, email);
                users.add(user);
            }
            if (users.isEmpty()) { return null; }
            else { return users; }
        }
        catch (SQLException e) {
            throw(new DataAccessException("Unable to access database"));
        }
    }
    public void newUser(UserData userData) throws DataAccessException {
        String sql = "INSERT INTO user(username, email, password) VALUES(?,?,?)";
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement(sql);
            statement.setString(1, userData.username());
            statement.setString(2, userData.email());
            statement.setString(3, userData.password());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw(new DataAccessException("Unable to access database"));
        }
    }
    public UserData getUser(String username) throws DataAccessException {
        String sql = "SELECT * FROM user WHERE username=?";
        try (var conn = DatabaseManager.getConnection()) {
            var statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String name = result.getString("username");
                String email = result.getString("email");
                String password = result.getString("password");
                return new UserData(name, password, email);
            }
            return null;
        }
        catch (SQLException e) {
            throw(new DataAccessException("Unable to access database"));
        }
    }


}
