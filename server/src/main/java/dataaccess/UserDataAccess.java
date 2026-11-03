package dataaccess;

import model.UserData;

import java.util.Collection;

public interface UserDataAccess {
    void clearData() throws DataAccessException;
    Collection<UserData> getUsers() throws DataAccessException;
    void newUser(UserData userData) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
}
