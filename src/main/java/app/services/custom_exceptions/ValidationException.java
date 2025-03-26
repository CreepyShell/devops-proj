package app.services.custom_exceptions;

public class ValidationException extends RuntimeException{
    public ValidationException(String message){
        super(message);
    }
}
