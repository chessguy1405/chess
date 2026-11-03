package service;

import dataaccess.*;
import dataaccess.sql.*;
import model.*;

public class LoginService {
    public static LoginResult login(LoginRequest request) throws DataAccessException {
        UserDataAccess userAccess = new UserDataAccessSQL();
        AuthDataAccess authAccess = new AuthDataAccessSQL();
        UserData existingUser = userAccess.getUser(request.username());
        if (existingUser == null) {
            throw new DoesNotExistException("User does not exist");
        }
        if (!existingUser.password().equals(request.password())) {
            throw new BadPasswordException("Incorrect password");
        }
        AuthData auth = authAccess.newAuth(request.username());
        return (new LoginResult(auth.username(), auth.authToken()));
    }
}
