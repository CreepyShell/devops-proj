package proj.services.custom_exceptions;

public class BalanceException extends RuntimeException {
    public BalanceException(String message){
        super(message);
    }
}
