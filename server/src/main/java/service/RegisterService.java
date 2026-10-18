package service;

import dataaccess.AuthDataAccess;
import dataaccess.AuthDataAccessMem;
import dataaccess.UserDataAccess;
import dataaccess.UserDataAccessMem;
import model.AuthData;
import model.UserData;
import model.LoginResult;

import java.util.UUID;

public class RegisterService {
    public static LoginResult register(UserData newUser) throws UserExistsException {
        UserDataAccess userAccess = new UserDataAccessMem();
        AuthDataAccess authAccess = new AuthDataAccessMem();
        UserData existingUser = userAccess.getUser(newUser.username());
        if (existingUser != null) { throw new UserExistsException("User already exists"); }
        userAccess.newUser(newUser);
        AuthData auth = authAccess.newAuth(newUser.username());
        return(new LoginResult(auth.authToken(), auth.username()));
    }
}
