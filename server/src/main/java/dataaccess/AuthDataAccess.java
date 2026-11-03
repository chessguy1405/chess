package dataaccess;

import model.AuthData;

public interface AuthDataAccess {
    AuthData newAuth(String username) throws DataAccessException;
    AuthData getAuth(String checkString) throws DataAccessException;
    void removeAuth(AuthData authData) throws DataAccessException;
    void clearData() throws DataAccessException;
}
