package client;

import chess.*;
import model.GameData;
import ui.*;

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

        if (facade.isViewing()) {
            GameData currentGame = facade.getCurrentGame();
            ChessGame.TeamColor view = ChessGame.TeamColor.WHITE;

            if (facade.isPlaying() && facade.getCurrentUser().equals(currentGame.blackUsername())) {
                view = ChessGame.TeamColor.BLACK;
            }

            BoardRenderer.printBoard(currentGame.game(), view);
        }

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
        if (!input.matches("[1-5]")) { return input; }

        return switch (getMenuState(facade)) {
            case LOGGED_OUT:
                yield switch (input) {
                    case "1" -> "login";
                    case "2" -> "register";
                    case "3" -> "quit";
                    default -> input;
                };
            case LOBBY:
                yield switch (input) {
                    case "1" -> "create";
                    case "2" -> "join";
                    case "3" -> "enter";
                    case "4" -> "logout";
                    case "5" -> "quit";
                    default -> input;
                };
            case IN_GAME:
                yield switch (input) {
                    case "1" -> "exit";
                    case "2" -> "quit";
                    default -> input;
                };
        };
    }

    private static String getParam(Scanner scanner, String message) {
        System.out.println(message);
        System.out.print(">>> ");
        if (!scanner.hasNextLine()) { return null; }
        return scanner.nextLine().trim();
    }

    private static String getPassword(Scanner scanner, String message) {
        System.out.println(message);
        System.out.print(">>> ");
        if (!scanner.hasNextLine()) { return null; }
        return scanner.nextLine();
    }

    private static void login(Scanner scanner, ServerFacade facade) {
        String username = getParam(scanner, "Please enter your username");
        if (username == null) { return; }

        String password = getPassword(scanner, "Please enter your password");
        if (password == null) { return; }

        facade.loginUser(username, password);
    }

    private static void register(Scanner scanner, ServerFacade facade) {
        String username = getParam(scanner, "Please enter your username");
        if (username == null) { return; }

        String password = getPassword(scanner, "Please enter your password");
        if (password == null) { return; }

        String confirmPassword = getPassword(scanner, "Please confirm your password");
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords must match");
            return;
        }

        String email = getParam(scanner, "Please enter your email");
        if (email == null) { return; }

        facade.registerUser(username, password, email);
    }

    private static boolean runCommand(Scanner scanner, ServerFacade facade, String input) {
        return switch(getMenuState(facade)) {
            case LOGGED_OUT -> runLoggedOutCommand(scanner, facade, input);
            case LOBBY -> runLobbyCommand(scanner, facade, input);
            case IN_GAME -> runInGameCommand(scanner, facade, input);
        };
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
            case "quit":
                yield false;
            case "exit":
                exit(scanner, facade);
                yield true;
            default:
                if (!input.isBlank()) {
                    System.out.println("Unknown command " + input);
                }
                yield true;
        };
    }

    static private void enter(Scanner scanner, ServerFacade facade) {
        var gameList = facade.listGames();
        if (gameList == null) { return; }
        Map<String, String> localIdList = new HashMap<>();
        int counter = 1;
        for (GameData game : gameList) {
            if (!game.game().isOver()) {
                localIdList.put(String.valueOf(counter), game.gameName());
                System.out.println("(" + counter++ + ") (" + game.gameName() + ")");
            }
        }
        if (localIdList.isEmpty()) {
            System.out.println("No games are currently active");
            return;
        }
        String selection = getParam(scanner, "Choose a game");
        if (selection == null) { return; }

        String gameName = localIdList.get(selection);
        if (gameName == null) {
            if (selection.matches("\\d+")) {
                System.out.println("Hmmm, that game doesn't seem to exist");
                return;
            }
            gameName = selection;
        }
        facade.enterGame(gameName);
    }

    static private void create(Scanner scanner, ServerFacade facade) {
        String gameName = getParam(scanner, "Choose a name for your game");
        if (gameName == null) { return; }
        if (gameName.matches("\\d+")) gameName = "game" + gameName;
        facade.createGame(gameName);
    }

    static private void join(Scanner scanner, ServerFacade facade) {
        var gameList = facade.listGames();
        if (gameList == null) { return; }

        Map<String, String> localIdList = new HashMap<>();
        int counter = 1;

        for (GameData game : gameList) {
            boolean hasOpenSeat = game.whiteUsername() == null || game.blackUsername() == null;

            if (!game.game().isOver() && hasOpenSeat) {
                localIdList.put(String.valueOf(counter), game.gameName());
                System.out.println("(" + counter++ + ") (" + game.gameName() + ")");
            }
        }

        if (localIdList.isEmpty()) {
            System.out.println("There are no active games with an open seat.");
        }

        String selection = getParam(scanner, "Choose a game");
        if (selection == null) { return; }

        String gameName = localIdList.get(selection);
        if (gameName == null) {
            if (selection.matches("\\d+")) {
                System.out.println("That game number doesn't exist");
                return;
            }
            gameName = selection;
        }

        String color = getParam(scanner, "Please enter your color (white or black)");
        if (color == null) { return; }

        facade.joinGame(gameName, color);
    }

    static private void exit(Scanner scanner, ServerFacade facade) {
        facade.exitGame();
    }
}
