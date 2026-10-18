package dataaccess;

import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserDataAccessMem implements UserDataAccess {

    static Map<String, UserData> users = new HashMap<>();

    public void clearData() {
        users = new HashMap<>();
    }

    public void newUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    public Collection<UserData> getUsers() {
        if (users.isEmpty()) { return null; }
        return(users.values());
    }

    public UserData getUser(String username) {
        return(users.get(username));
    }
}
