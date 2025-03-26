package app.services.custom_exceptions;

public class TicketException extends RuntimeException{
    public TicketException(String message){
        super(message);
    }
}
