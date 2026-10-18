package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthDataAccessMem implements AuthDataAccess {
    Map<String, AuthData> tokens;

    public AuthDataAccessMem() {
        tokens = new HashMap<>();
    }

    public AuthData newAuth(String username) {
        return null;
    }

    public AuthData getAuth(String username) {
        return null;
    }

    public void removeAuth(AuthData authData) {}
}
