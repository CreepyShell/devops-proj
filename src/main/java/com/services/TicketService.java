package com.services;

import com.models.*;
import com.services.custom_exceptions.BalanceException;
import com.services.custom_exceptions.RouteException;
import com.services.custom_exceptions.TicketException;
import com.services.interfaces.ITicketService;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class TicketService implements ITicketService {
    private PlaneDb db;

    public TicketService(PlaneDb db) {
        this.db = db;
    }

    @Override
    public Ticket buyTicket(User user, Route route) {
        List<User> users = db.getUsers();
        User dbUser = users.stream().filter(us -> us.getId().equals(user.getId())).findAny().orElse(null);
        Route dbRoute = db.getRoutes().stream().filter(r -> r.getId().equals(route.getId())).findAny().orElse(null);

        if (dbRoute == null || dbUser == null)
            throw new InvalidParameterException("User or route was not found");

        if (!checkAvailableTickets(route)) {
            throw new RouteException("No available tickets on this route!!!");
        }

        if (user.getBalance() < route.getCost()) {
            throw new BalanceException("User does not have enough money to but the tickets");
        }
        users.remove(user);
        user.setBalance(user.getBalance() - route.getCost());
        users.add(user);
        db.setUsers(users);

        Ticket ticket = new Ticket(Calendar.getInstance().getTime(), dbUser, dbRoute.createCopy());

        List<Ticket> tickets = db.getTickets();
        tickets.add(ticket);
        db.setTickets(tickets);
        return ticket;
    }

    @Override
    public Ticket rescheduleTrip(User user, Ticket ticket, Route otherRoute) {
        User dbUser = db.getUsers().stream().filter(us -> us.getId().equals(user.getId())).findAny().orElse(null);
        Ticket dbTicket = db.getTickets().stream().filter(t -> t.getId().equals(ticket.getId())).findAny().orElse(null);
        Route dbOtherRoute = db.getRoutes().stream().filter(r -> r.getId().equals(otherRoute.getId())).findAny().orElse(null);
        Route dbRoute = db.getRoutes().stream().filter(r -> r.getId().equals(ticket.getRoute().getId())).findAny().orElse(null);

        if (dbTicket == null || dbUser == null || dbOtherRoute == null || dbRoute == null)
            throw new InvalidParameterException("User, ticket or another route did not find");

        long timeDifference = dbRoute.getTakeOffTime().getTime() - Calendar.getInstance().getTime().getTime();
        if ((timeDifference / (1000 * 60 * 60 * 24)) > 7) {
            throw new TicketException("You can not reschedule trip less than 7 days before take off");
        }

        if (dbOtherRoute.getCost() > dbRoute.getCost()) {
            throw new BalanceException("Chosen other trip is more expensive than previous");
        }

        List<Ticket> tickets = db.getTickets();
        tickets.remove(ticket);
        db.setTickets(tickets);

        return buyTicket(user, otherRoute);
    }

    @Override
    public boolean cancelTicket(User user, Ticket ticket) {
        List<User> users = db.getUsers();
        User dbUser = users.stream().filter(us -> us.getId().equals(user.getId())).findAny().orElse(null);
        Ticket dbTicket = db.getTickets().stream().filter(t -> t.getId().equals(ticket.getId())).findAny().orElse(null);

        if (dbTicket == null || dbUser == null)
            throw new InvalidParameterException("User, ticket or another route did not find");

        long timeDifference = dbTicket.getRoute().getTakeOffTime().getTime() - Calendar.getInstance().getTime().getTime();
        if ((timeDifference / (1000 * 60 * 60 * 24)) < 7) {
            return false;
        }

        users.remove(dbUser);
        dbUser.setBalance(dbUser.getBalance() + ticket.getRoute().getCost() * 0.9);
        users.add(dbUser);
        db.setUsers(users);

        List<Ticket> tickets = db.getTickets();
        tickets.remove(ticket);
        db.setTickets(tickets);
        return true;
    }

    @Override
    public Ticket getTicketById(String id) {
        return db.getTickets().stream().filter(t -> t.getId().equals(id)).findAny().orElse(null);
    }

    @Override
    public Ticket[] buildTrip(Location startLocation, Location endLocation) {
        return new Ticket[0];
    }

    @Override
    public Ticket[] getTicketsByUserId(String userId) {
        List<Ticket> tickets = db.getTickets().stream().filter(t -> Objects.equals(t.getUser().getId(), userId)).toList();
        return tickets.toArray(new Ticket[0]);
    }

    private boolean checkAvailableTickets(Route route) {
        List<Ticket> tickets = db.getTickets().stream().filter(t -> Objects.equals(t.getRoute().getId(), route.getId())).toList();
        return tickets.size() < route.getPlane().getMaxAmountOfSeats();
    }
}
