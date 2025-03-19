package proj.services.custom_exceptions;

public class TicketException extends RuntimeException{
    public TicketException(String message){
        super(message);
    }
}
