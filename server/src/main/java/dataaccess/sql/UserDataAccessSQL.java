package dataaccess.sql;

import dataaccess.*;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class UserDataAccessSQL implements UserDataAccess {

    public void clearData() {}
    public Collection<UserData> getUsers() {return null;}
    public void newUser(UserData userData) {}
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
