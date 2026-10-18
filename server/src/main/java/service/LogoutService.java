package service;

import dataaccess.*;
import model.*;

public class LogoutService {
    public static void logout(LogoutRequest request) {
        AuthDataAccess authAccess = new AuthDataAccessMem();
        AuthData auth = authAccess.getAuth(request.authToken());
        if (auth == null) { throw new DoesNotExistException("AuthToken is not valid"); }
        authAccess.removeAuth(auth);
    }
}
