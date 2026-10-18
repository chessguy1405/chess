package service;

public class TeamTakenException extends RuntimeException {
    public TeamTakenException(String message) {
        super(message);
    }
}
