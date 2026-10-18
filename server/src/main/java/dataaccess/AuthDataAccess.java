package dataaccess;

import model.AuthData;

public interface AuthDataAccess {
    AuthData newAuth(String username);
    AuthData getAuth(String checkString);
    void removeAuth(AuthData authData);
    void clearData();
}
