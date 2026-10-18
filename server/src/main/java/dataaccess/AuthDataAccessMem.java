package dataaccess;

import model.AuthData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthDataAccessMem implements AuthDataAccess {
    static Map<String, AuthData> tokens = new HashMap<>();
    static Map<String, AuthData> users = new HashMap<>();

    public void clearData() {
        tokens = new HashMap<>();
    }

    public AuthData newAuth(String username) {
        String token = UUID.randomUUID().toString();
        AuthData authData = new AuthData(token, username);
        tokens.put(username, authData);
        users.put(token, authData);
        return(authData);
    }

    public AuthData getAuth(String checkString) {
        if (tokens.get(checkString) == null) {
            return (users.get(checkString));
        }
        return tokens.get(checkString);
    }


    public void removeAuth(AuthData authData) {
        String username = authData.username();
        String token = authData.authToken();
        tokens.remove(username);
        users.remove(token);

    }
}
