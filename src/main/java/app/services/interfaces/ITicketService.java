package app.services.interfaces;

import app.models.Location;
import app.models.Route;
import app.models.Ticket;
import app.models.User;

public interface ITicketService {
    Ticket buyTicket(User user, Route route);

    Ticket rescheduleTrip(User user, Ticket ticket, Route otherRoute);

    boolean cancelTicket(User user, Ticket ticket);
    Ticket getTicketById(String id);

    Ticket[] buildTrip(Location startLocation, Location endLocation);
    Ticket[] getTicketsByUserId(String userId);
}
