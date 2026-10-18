package service;

import dataaccess.*;
import model.*;

public class LoginService {
    public static LoginResult login(LoginRequest request) {
        UserDataAccess userAccess = new UserDataAccessMem();
        AuthDataAccess authAccess = new AuthDataAccessMem();
        UserData existingUser = userAccess.getUser(request.username());
        if (existingUser == null) { throw new DoesNotExistException("User does not exist"); }
        if (!existingUser.password().equals(request.password())) { throw new BadPasswordException("Incorrect password"); }
        AuthData auth = authAccess.getAuth(request.username());
        if (auth == null) {
            auth = authAccess.newAuth(request.username());
        }
        return(new LoginResult(auth.username(), auth.authToken()));
    }
}
