package dataaccess;

import model.*;
import java.util.*;

public class InMemoryStorage {

    Map<Integer, UserData> users;
    Map<String, GameData> games;
    Map<String, AuthData> tokens;

    InMemoryStorage() {
        users = new HashMap<>();
        games = new HashMap<>();
        tokens = new HashMap<>();
    }
}
