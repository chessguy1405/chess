package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthDataAccessMem implements AuthDataAccess {
    static Map<String, AuthData> tokens = new HashMap<>();

    public void clearData() {
        tokens = new HashMap<>();
    }

    public AuthData newAuth(String username) {
        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, username);
        tokens.put(username, authData);
        return(authData);
    }

    public AuthData getAuth(String username) {
        return(tokens.get("username"));
    }

    public void removeAuth(AuthData authData) {
        String username = authData.username();
        tokens.remove(username);
    }
}
