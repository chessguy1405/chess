package dataaccess;

import model.UserData;

import java.sql.SQLException;
import java.util.Collection;

public class UserDataAccessSQL implements UserDataAccess {
    public void clearData() {}
    public Collection<UserData> getUsers() {return null;}
    public void newUser(UserData userData) {}
    public UserData getUser(String username) {return null;}

    static private void makeUserTable() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();) {
            var statement = "CREATE TABLE IF NOT EXISTS user ( " +
                    "id INT AUTO_INCREMENT PRIMARY KEY NOT NULL," +
                    "username VARCHAR(100) UNIQUE NOT NULL," +
                    "email VARCHAR(100) UNIQUE NOT NULL," +
                    "password VARCHAR(255) NOT NULL)";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            throw(new DataAccessException("Unable to connect to the database"));
        }
    }
}
