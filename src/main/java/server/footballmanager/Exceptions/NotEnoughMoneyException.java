package server.footballmanager.Exceptions;

public class NotEnoughMoneyException extends RuntimeException{
    public NotEnoughMoneyException(Long id) {
        super("Team " + id + "does not have enough money for this operation.");
    }
}
