package client;

import chess.*;

import java.util.Locale;
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

                switch(getMenuState(facade)) {
                    case LOGGED_OUT:
                        switch (input.toLowerCase(Locale.ROOT)) {
                            case "quit":
                                running = false;
                                break;
                            case "login":
                                login(scanner, facade);
                                break;
                            case "register":
                                register(scanner, facade);
                                break;
                            default:
                                if (!input.isBlank()) {
                                    System.out.println("Unknown command " + input);
                                }
                                break;
                        }
                        break;
                    case LOBBY:
                        switch (input.toLowerCase(Locale.ROOT)) {
                            case "quit":
                                running = false;
                                break;
                            case "logout":
                                facade.logoutUser();
                                break;
                            default:
                                if (!input.isBlank()) {
                                    System.out.println("Unknown command " + input);
                                }
                                break;
                        }
                        break;
                    case IN_GAME:
                        switch (input.toLowerCase(Locale.ROOT)) {
                            default:
                                if (!input.isBlank()) {
                                    System.out.println("Unknown command " + input);
                                }
                                break;
                        }
                        break;
                    default: System.out.println("Corrupted state. Please relaunch the application.");
                }
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
        if (!password.equals(confirmPassword)) return;

        String email = getParam(scanner, "Please enter your email");
        if (email == null) return;

        facade.registerUser(username, password, email);
    }
}
