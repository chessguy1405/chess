package service;

import chess.ChessGame;
import dataaccess.*;
import model.*;

public class CreateGameService {
    public static NewGameResult newGame(NewGameRequest request) throws DoesNotExistException {
        AuthDataAccess authAccess = new AuthDataAccessMem();
        GameDataAccess gameAccess = new GameDataAccessMem();

        AuthData auth = authAccess.getAuth(request.authToken());
        if (auth == null) { throw new DoesNotExistException("AuthToken is not valid"); }
        else {
            int gameId = gameAccess.nextId();
            GameData gameData = new GameData(gameId, null, null, request.gameName(), new ChessGame());
            gameAccess.newGame(gameData);
            return (new NewGameResult(gameId));
        }
    }
}
