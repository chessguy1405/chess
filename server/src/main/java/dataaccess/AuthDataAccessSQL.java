package dataaccess;

import model.AuthData;

import java.sql.DriverManager;
import java.sql.SQLException;

public class AuthDataAccessSQL implements AuthDataAccess {
    AuthDataAccessSQL() throws DataAccessException {
        makeAuthTable();
    }

    public AuthData newAuth(String username) {
        return null;
    }
    public AuthData getAuth(String checkString) {
        return null;
    }
    public void removeAuth(AuthData authData) {}
    public void clearData() {}

    static private void makeAuthTable() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();) {
            var statement = "CREATE TABLE IF NOT EXISTS auth ( " +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "authtoken VARCHAR(255) UNIQUE," +
                    "username VARCHAR(100))";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e) {
            throw(new DataAccessException("Unable to connect to the database"));
        }
    }
}
