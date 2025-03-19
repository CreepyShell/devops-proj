package proj.services.interfaces;

import proj.models.Location;
import proj.models.Route;
import proj.models.Ticket;
import proj.models.User;

public interface ITicketService {
    Ticket buyTicket(User user, Route route);

    Ticket rescheduleTrip(User user, Ticket ticket, Route otherRoute);

    boolean cancelTicket(User user, Ticket ticket);
    Ticket getTicketById(String id);

    Ticket[] buildTrip(Location startLocation, Location endLocation);
    Ticket[] getTicketsByUserId(String userId);
}
