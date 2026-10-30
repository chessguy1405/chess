package dataaccess;

import model.UserData;

import java.util.Collection;

public class UserDataAccessSQL implements UserDataAccess {
    public void clearData() {}
    public Collection<UserData> getUsers() {return null;}
    public void newUser(UserData userData) {}
    public UserData getUser(String username) {return null;}
}
