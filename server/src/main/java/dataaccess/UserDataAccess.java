package dataaccess;

import model.UserData;

import java.util.Collection;

public interface UserDataAccess {
    void clearData();
    Collection<UserData> getUsers();
    void newUser(UserData userData);
}
