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
                System.out.print(">>>");
                if (!scanner.hasNextLine()) {
                    break;
                }

                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("exit")) {
                    running = false;
                }
                else if (!input.isEmpty()) {
                    System.out.println("Unknown command " + input);
                }
            }
        }
    }
}
