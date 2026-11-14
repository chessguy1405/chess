package client;

import chess.*;
import java.util.Scanner;

public class ClientMain {
    private static final String SERVER_URL = "http://localhost:8080";

    public static void main(String[] args) {

        ServerFacade facade = new ServerFacade(SERVER_URL);
        String systemPrompt = "What would you like to do?";

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                System.out.println();
                System.out.println(systemPrompt);
                printState(facade);
                if (!scanner.hasNextLine()) {
                    break;
                }

                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("quit")) {
                    running = false;
                }
                else if (!input.isEmpty()) {
                    System.out.println("Unknown command " + input);
                }
            }
        }
    }

    private static void printState(ServerFacade facade) {
        String message = facade.getServerMessage();
        if (message != null && !message.isBlank()) {
            System.out.println(message);
        }

        if (!facade.isLoggedIn()) {
            System.out.println("1. (Login)");
            System.out.println("2. (Register)");
            System.out.println("3. (Quit) Program");
            System.out.print("[LOGGED OUT] >>>");
        }
        else if (!facade.isViewing()) {
            System.out.println("1. (Create) Game");
            System.out.println("2. (Join) Game");
            System.out.println("3. (Enter) Game");
            System.out.println("4. (Logout)");
            System.out.println("5. (Quit) Program");
            System.out.print("[LOGGED IN] >>>");
        }
        else if (!facade.isPlaying()) {
            System.out.println("1. (Exit) Game");
            System.out.println("2. (Quit) Program");
            System.out.print("[OBSERVING] >>>");
        }
        else {
            System.out.println("1. (Exit) Game");
            System.out.println("2. (Quit) Program");
            System.out.print("[PLAYING] >>>");
        }
    }
}
