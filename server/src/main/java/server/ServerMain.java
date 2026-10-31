package server;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;

public class ServerMain {
    public static void main(String[] args) {
        var port = 8080;
        Server server = new Server();
        server.run(port);

        System.out.println("♕ 240 Chess Server running on port " + port);

        try {
            DatabaseManager.createDatabase();
            DatabaseManager.makeAuthTable();
            DatabaseManager.makeGameTable();
            DatabaseManager.makeUserTable();
        }
        catch(DataAccessException e) {
            throw new RuntimeException("Uh Oh! Something went wrong!");
        }
    }
}