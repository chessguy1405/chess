package service;

import java.util.*;

import dataaccess.AuthDataAccess;
import dataaccess.AuthDataAccessMem;
import dataaccess.GameDataAccess;
import dataaccess.GameDataAccessMem;
import model.*;

public class ListGameService {
    public static ListGameResult listGames(ListGameRequest request) throws DoesNotExistException {
        AuthDataAccess authAccess = new AuthDataAccessMem();
        GameDataAccess gameAccess = new GameDataAccessMem();

        AuthData auth = authAccess.getAuth(request.authToken());
        if (auth == null) { throw new DoesNotExistException("AuthToken is not valid"); }
        Collection<GameData> gameList = gameAccess.getGames();
        return (new ListGameResult(gameList));
    }
}
