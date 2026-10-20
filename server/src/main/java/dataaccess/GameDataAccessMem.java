package dataaccess;

import chess.ChessGame;
import model.GameData;
import service.TeamTakenException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameDataAccessMem implements GameDataAccess {
   private static Map<Integer, GameData> games = new HashMap<>();
   private static int maxId = 1;

    public void clearData() {
        games = new HashMap<>();
    }

    public void newGame(GameData gameData) {
        games.put(gameData.gameID(), gameData);
        maxId++;
    }

    public Collection<GameData> getGames() {
        if (games.isEmpty()) { return null; }
        return(games.values());
    }

    public GameData getGame(int id) {
        return(games.get(id));
    }

    public int nextId() {
        return(maxId);
    }

    public void joinGame(ChessGame.TeamColor color, String username, int gameId) throws DataAccessException {
        GameData existingGame = getGame(gameId);
        String whiteUsername = existingGame.whiteUsername();
        String blackUsername = existingGame.blackUsername();
        ChessGame game = existingGame.game();
        String gameName = existingGame.gameName();
        if (color.equals(ChessGame.TeamColor.WHITE)) {
            if (whiteUsername != null) { throw new DataAccessException("White is taken"); }
            else { whiteUsername = username; }
        }
        else if (color.equals(ChessGame.TeamColor.BLACK)) {
            if (blackUsername != null) { throw new DataAccessException("Black is taken"); }
            else { blackUsername = username; }
        }
        GameData gameData = new GameData(gameId, whiteUsername, blackUsername, gameName, game);
        games.put(gameId, gameData);
    }
}
