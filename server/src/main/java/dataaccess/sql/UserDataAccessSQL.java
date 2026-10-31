package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDataAccess;
import model.UserData;

import java.sql.SQLException;
import java.util.Collection;

public class UserDataAccessSQL implements UserDataAccess {

    public void clearData() {}
    public Collection<UserData> getUsers() {return null;}
    public void newUser(UserData userData) {}
    public UserData getUser(String username) {return null;}


}
