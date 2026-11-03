package service;

import java.util.*;

import dataaccess.*;
import dataaccess.sql.*;
import model.*;

public class ListGameService {
    public static ListGameResult listGames(ListGameRequest request) throws DoesNotExistException, DataAccessException {
        AuthDataAccess authAccess = new AuthDataAccessSQL();
        GameDataAccess gameAccess = new GameDataAccessSQL();

        AuthData auth = authAccess.getAuth(request.authToken());
        if (auth == null) { throw new DoesNotExistException("AuthToken is not valid"); }
        Collection<GameData> gameList = gameAccess.getGames();
        ListGameResult result;
        if (gameList == null) {
            result = new ListGameResult(new ArrayList<>());
        } else {
            result = new ListGameResult(gameList);
        }
        return(result);
    }
}
