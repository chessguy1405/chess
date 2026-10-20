package service;

import dataaccess.*;
import model.*;
import chess.ChessGame;

public class JoinGameService {
    public static void joinGame(JoinGameRequest request) {
        AuthDataAccess authAccess = new AuthDataAccessMem();
        GameDataAccess gameAccess = new GameDataAccessMem();
        AuthData auth = authAccess.getAuth(request.authToken());
        if (auth == null) { throw new DoesNotExistException("AuthToken is not valid"); }
        else {
            try {
                ChessGame.TeamColor color = request.pieceColor();
                int gameId = request.gameId();
                String username = auth.username();
                GameData existingGame = gameAccess.getGame(gameId);
                if (existingGame == null) { throw new BadGameException("Game does not exist"); }
                gameAccess.joinGame(color, username, gameId);
            }
            catch (DataAccessException e) {
                throw new TeamTakenException("Team has already been taken");
            }
        }
    }
}
