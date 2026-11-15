package client;

import chess.*;
import model.GameData;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class ClientMain {
    private static final String SERVER_URL = "http://localhost:8080";

    private enum MenuState {
        LOGGED_OUT,
        LOBBY,
        IN_GAME
    }

    public static void main(String[] args) {

        ServerFacade facade = new ServerFacade(SERVER_URL);

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                System.out.println();
                printState(facade);
                if (!scanner.hasNextLine()) {
                    break;
                }

                String input = scanner.nextLine().trim();
                input = normalizeCommand(input, facade);

                running = runCommand(scanner, facade, input);
            }
        }
    }

    private static MenuState getMenuState(ServerFacade facade) {
        if (!facade.isLoggedIn()) {
            return MenuState.LOGGED_OUT;
        }
        if (facade.isViewing()) {
            return MenuState.IN_GAME;
        }
        return MenuState.LOBBY;
    }

    private static void printState(ServerFacade facade) {
        String message = facade.getServerMessage();
        if (message != null && !message.isBlank()) {
            System.out.println(message);
        }

        if (!facade.isLoggedIn()) {
            System.out.println("(1) (Login)");
            System.out.println("(2) (Register)");
            System.out.println("(3) (Quit) Program");
            System.out.print("[LOGGED OUT] >>>");
        }
        else if (!facade.isViewing()) {
            System.out.println("(1) (Create) Game");
            System.out.println("(2) (Join) Game");
            System.out.println("(3) (Enter) Game");
            System.out.println("(4) (Logout)");
            System.out.println("(5) (Quit) Program");
            System.out.print("[LOGGED IN] >>>");
        }
        else if (!facade.isPlaying()) {
            System.out.println("(1) (Exit) Game");
            System.out.println("(2) (Quit) Program");
            System.out.print("[OBSERVING] >>>");
        }
        else {
            System.out.println("(1) (Exit) Game");
            System.out.println("(2) (Quit) Program");
            System.out.print("[PLAYING] >>>");
        }
    }

    private static String normalizeCommand(String input, ServerFacade facade) {
        if (!input.matches("[1-5]")) return input;

        switch (getMenuState(facade)) {
            case LOGGED_OUT:
                switch (input) {
                    case "1": return "login";
                    case "2": return "register";
                    case "3": return "quit";
                    default: return input;
                }
            case LOBBY:
                switch (input) {
                    case "1": return "create";
                    case "2": return "join";
                    case "3": return "enter";
                    case "4": return "logout";
                    case "5": return "quit";
                    default: return input;
                }
            case IN_GAME:
                switch (input) {
                    case "1": return "exit";
                    case "2": return "quit";
                    default: return input;
                }
            default: return input;
        }
    }

    private static String getParam(Scanner scanner, String message) {
        System.out.println(message);
        System.out.print(">>> ");
        if (!scanner.hasNextLine()) return null;
        return scanner.nextLine().trim();
    }

    private static void login(Scanner scanner, ServerFacade facade) {
        String username = getParam(scanner, "Please enter your username");
        if (username == null) return;

        String password = getParam(scanner, "Please enter your password");
        if (password == null) return;

        facade.loginUser(username, password);
    }

    private static void register(Scanner scanner, ServerFacade facade) {
        String username = getParam(scanner, "Please enter your username");
        if (username == null) return;

        String password = getParam(scanner, "Please enter your password");
        if (password == null) return;

        String confirmPassword = getParam(scanner, "Please confirm your password");
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords must match");
            return;
        }

        String email = getParam(scanner, "Please enter your email");
        if (email == null) return;

        facade.registerUser(username, password, email);
    }

    private static boolean runCommand(Scanner scanner, ServerFacade facade, String input) {
        switch(getMenuState(facade)) {
            case LOGGED_OUT:
                return runLoggedOutCommand(scanner, facade, input);
            case LOBBY:
                return runLobbyCommand(scanner, facade, input);
            case IN_GAME:
                return runInGameCommand(scanner, facade, input);
            default:
                System.out.println("Corrupted state. Please relaunch the application.");
                return false;
        }
    }

    private static boolean runLoggedOutCommand(Scanner scanner, ServerFacade facade, String input) {
        return switch (input.toLowerCase(Locale.ROOT)) {
            case "quit": yield false;
            case "login":
                login(scanner, facade);
                yield true;
            case "register":
                register(scanner, facade);
                yield true;
            default:
                if (!input.isBlank()) {
                    System.out.println("Unknown command " + input);
                }
                yield true;
        };
    }

    static private boolean runLobbyCommand(Scanner scanner, ServerFacade facade, String input) {
        return switch (input.toLowerCase(Locale.ROOT)) {
            case "quit":
                yield false;
            case "logout":
                facade.logoutUser();
                yield true;
            case "enter":
                enter(scanner, facade);
                yield true;
            case "join":
                join(scanner, facade);
                yield true;
            case "create":
                create(scanner, facade);
                yield true;
            default:
                if (!input.isBlank()) {
                    System.out.println("Unknown command " + input);
                }
                yield true;
        };
    }

    static private boolean runInGameCommand(Scanner scanner, ServerFacade facade, String input) {
        return switch (input.toLowerCase(Locale.ROOT)) {
            default:
                if (!input.isBlank()) {
                    System.out.println("Unknown command " + input);
                }
                yield true;
        };
    }

    static private void enter(Scanner scanner, ServerFacade facade) {
        var gameList = facade.listGames();
        Map<String, String> localIdList = new HashMap<>();
        int counter = 1;
        for (GameData game : gameList) {
            if (!game.game().isOver()) {
                localIdList.put(String.valueOf(counter), game.gameName());
                System.out.println("(" + counter++ + ") (" + game.gameName() + ")");
            }
        }
        String gameName = getParam(scanner, "Choose a game");
        if (gameName == null) return;
        if (gameName.matches("[0-9]*")) {
            if (Integer.parseInt(gameName) >= counter) {
                System.out.println("Hmmm, that game doesn't seem to exist");
                return;
            }
            gameName = localIdList.get(gameName);
        }
        facade.enterGame(gameName);
    }

    static private void create(Scanner scanner, ServerFacade facade) {
        String gameName = getParam(scanner, "Choose a name for your game");
        facade.createGame(gameName);
    }
}
