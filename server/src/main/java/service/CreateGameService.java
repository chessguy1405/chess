package service;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.sql.*;
import model.*;

import java.sql.SQLException;

public class CreateGameService {
    public static NewGameResult newGame(NewGameRequest request) throws DoesNotExistException, DataAccessException {
        AuthDataAccess authAccess = new AuthDataAccessSQL();
        GameDataAccess gameAccess = new GameDataAccessSQL();

        AuthData auth = authAccess.getAuth(request.authToken());
        if (auth == null) {
            throw new DoesNotExistException("AuthToken is not valid");
        } else {
            int gameId = gameAccess.nextId();
            GameData gameData = new GameData(gameId, null, null, request.gameName(), new ChessGame());
            gameAccess.newGame(gameData);
            return (new NewGameResult(gameId));
        }
    }
}
