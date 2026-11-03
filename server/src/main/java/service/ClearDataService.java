package service;

import dataaccess.*;
import dataaccess.sql.*;

public class ClearDataService  {
    public static void clearAllData() throws DataAccessException {
        AuthDataAccess authAccess = new AuthDataAccessSQL();
        UserDataAccess userAccess = new UserDataAccessSQL();
        GameDataAccess gameAccess = new GameDataAccessSQL();

        authAccess.clearData();
        userAccess.clearData();
        gameAccess.clearData();
    }
}
