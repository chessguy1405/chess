package dataaccess.sql;

import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import java.sql.SQLException;

public class AuthDataAccessSQL implements AuthDataAccess {

    public AuthData newAuth(String username) {
        return null;
    }
    public AuthData getAuth(String checkString) {
        return null;
    }
    public void removeAuth(AuthData authData) {}
    public void clearData() {}


}
