package client;

import chess.*;
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

                if (input.equalsIgnoreCase("quit")) {
                    running = false;
                }
                else if (!input.isEmpty()) {
                    System.out.println("Unknown command " + input);
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
}
