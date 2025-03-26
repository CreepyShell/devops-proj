package app.models.Plane;

import app.models.Height;
import app.models.Location;
import app.models.Route;

import java.security.InvalidParameterException;
import java.util.List;

public class PrivatePlane extends Plane {
    public PrivatePlane() {
        super();
    }

    public PrivatePlane(int maxAmountOfSeats, Location location, double speed, List<Route> routes, Height height, double maxFlyDistance, String name) {
        super(maxAmountOfSeats, location, speed, routes, height, maxFlyDistance, name);
    }

    public void setMaxAmountOfSeats(int maxAmountOfSeats) {
        if (maxAmountOfSeats > 440 || maxAmountOfSeats < 0)
            throw new InvalidParameterException("Too many amount of seats in private plane or its value less than zero");
        this.maxAmountOfSeats = maxAmountOfSeats;
    }

    @Override
    public void setSpeed(double speed) {
        if (speed > 420 || speed < 0)
            throw new InvalidParameterException("Private plane too fast or its speed less than zero");
        this.speed = speed;
    }

    @Override
    public void setMaxFlyDistance(double maxFlyDistance) {
        if (maxFlyDistance > 14260400 || maxFlyDistance < 10000)
            throw new InvalidParameterException("Max fly distance of a private plane is too long or too short(can be between 10km and 14 120km)");
        this.maxFlyDistance = maxFlyDistance;
    }

    @Override
    public Plane createCopy() {
        PrivatePlane plane = new PrivatePlane(this.getMaxAmountOfSeats(), this.getLocation(), this.getSpeed(),
                List.copyOf(this.getRoutes()), this.getHeight(), this.getMaxFlyDistance(), this.getName());
        plane.setId(this.getId());
        return plane;
    }
}
