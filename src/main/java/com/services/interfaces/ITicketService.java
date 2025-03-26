package com.services.interfaces;

import com.models.Location;
import com.models.Route;
import com.models.Ticket;
import com.models.User;

public interface ITicketService {
    Ticket buyTicket(User user, Route route);

    Ticket rescheduleTrip(User user, Ticket ticket, Route otherRoute);

    boolean cancelTicket(User user, Ticket ticket);
    Ticket getTicketById(String id);

    Ticket[] buildTrip(Location startLocation, Location endLocation);
    Ticket[] getTicketsByUserId(String userId);
}
