package service;

import dataaccess.*;

public class ClearDataService {
    public static void clearAllData() {
        AuthDataAccess authAccess = new AuthDataAccessMem();
        UserDataAccess userAccess = new UserDataAccessMem();
        GameDataAccess gameAccess = new GameDataAccessMem();

        authAccess.clearData();
        userAccess.clearData();
        gameAccess.clearData();
    }
}
