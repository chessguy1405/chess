package service;

import dataaccess.*;
import dataaccess.sql.*;
import model.*;

public class LogoutService {
    public static void logout(LogoutRequest request) throws DataAccessException {
        AuthDataAccess authAccess = new AuthDataAccessSQL();
        AuthData auth = authAccess.getAuth(request.authToken());
        if (auth == null) { throw new DoesNotExistException("AuthToken is not valid"); }
        authAccess.removeAuth(auth);
    }
}
