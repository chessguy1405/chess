package service;

import dataaccess.*;
import dataaccess.sql.*;
import model.AuthData;
import model.UserData;
import model.LoginResult;

import org.mindrot.jbcrypt.BCrypt;

public class RegisterService {
    public static LoginResult register(UserData newUser) throws UserExistsException, DataAccessException {
        UserDataAccess userAccess = new UserDataAccessSQL();
        AuthDataAccess authAccess = new AuthDataAccessSQL();
        UserData existingUser = userAccess.getUser(newUser.username());
        if (existingUser != null) { throw new UserExistsException("User already exists"); }
        String hashedPassword = BCrypt.hashpw(newUser.password(), BCrypt.gensalt());
        UserData createdUser = new UserData(newUser.username(), hashedPassword, newUser.email());
        userAccess.newUser(createdUser);
        AuthData auth = authAccess.newAuth(newUser.username());
        return(new LoginResult(auth.username(), auth.authToken()));
    }
}
