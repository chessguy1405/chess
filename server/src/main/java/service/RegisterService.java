package service;

import model.UserData;
import model.LoginResult;

import java.util.UUID;

public class RegisterService {
    public static LoginResult register(UserData newUser) throws UserExistsException {
        return new LoginResult(UUID.randomUUID().toString(), newUser.username());
    }
}
