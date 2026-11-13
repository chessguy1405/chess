package client;

import model.*;

import java.util.List;

public class ServerFacade {

    static private String currentAuth;
    static private String currentUser;

    ServerFacade() {
        currentAuth = null;
        currentUser = null;
    }

    public boolean clearServerState() { return false; }

    public boolean loginUser(String username, String password) { return false; }

    public boolean logoutUser() { return false; }

    public boolean registerUser(String username, String password, String email) { return false; }

    public boolean joinGame(String name) { return false; }

    public GameData enterGame(String name) { return null; }

    public boolean createGame(String name) { return false; }

    public String getCurrentUser() { return(currentUser);}

    public boolean isLoggedIn() { return(currentAuth != null); }

    public List<GameData> listGames() { return null; }

}
