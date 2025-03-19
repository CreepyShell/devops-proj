package proj.services.custom_exceptions;

public class RouteException extends RuntimeException{
    public RouteException(String message){
        super(message);
    }
}
