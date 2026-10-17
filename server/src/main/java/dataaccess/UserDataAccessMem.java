package dataaccess;

import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserDataAccessMem implements UserDataAccess {

    Map<String, UserData> users;

    public UserDataAccessMem() {
        users = new HashMap<>();
    }

    public void clearData() {
        // Removes all user data from in-memory storage
    }

    public void newUser(UserData userData) {
        // Create a new game
    }

    public Collection<UserData> getUsers() {
        // Get a list of all users
        return null;
    }
}
