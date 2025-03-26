package app.models;

import java.util.Date;
import java.util.UUID;

public class Ticket {
    private String id;
    private Date dateBought;
    private Date dateReschedule;
    private User user;
    private Route route;

    public Ticket() {
        setId("");
    }

    public Ticket(Date dateBought, User user, Route route) {
        setId("");
        setDateBought(dateBought);
        setDateReschedule(dateBought);
        setUser(user);
        setRoute(route);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        if (id.length() == 0) {
            this.id = UUID.randomUUID().toString();
            return;
        }
        this.id = id;
    }

    public Date getDateBought() {
        return dateBought;
    }

    public void setDateBought(Date dateBought) {
        this.dateBought = dateBought;
    }

    public Date getDateReschedule() {
        return dateReschedule;
    }

    public void setDateReschedule(Date dateReschedule) {
        this.dateReschedule = dateReschedule;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
