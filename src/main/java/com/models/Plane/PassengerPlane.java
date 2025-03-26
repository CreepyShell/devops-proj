package com.models.Plane;

import com.models.Height;
import com.models.Location;
import com.models.Route;

import java.security.InvalidParameterException;
import java.util.List;

public class PassengerPlane extends Plane {
    public PassengerPlane() {
    }

    public PassengerPlane(int maxAmountOfSeats, Location location, double speed, List<Route> routes, Height height, double maxFlyDistance, String name) {
        super(maxAmountOfSeats, location, speed, routes, height, maxFlyDistance, name);
    }

    @Override
    public void setMaxAmountOfSeats(int maxAmountOfSeats) {
        if (maxAmountOfSeats > 440 || maxAmountOfSeats < 0)
            throw new InvalidParameterException("Too many amount of seats in passenger plane or its value less than zero");
        this.maxAmountOfSeats = maxAmountOfSeats;
    }

    @Override
    public void setSpeed(double speed) {
        if (speed > 300 || speed < 0)
            throw new InvalidParameterException("Passenger plane too fast or its speed less than zero");
        this.speed = speed;
    }

    @Override
    public void setMaxFlyDistance(double maxFlyDistance) {
        if (maxFlyDistance > 18000000 || maxFlyDistance < 100000)
            throw new InvalidParameterException("Max fly distance of a passenger plane is too long or too short(can be between 100km and 18 000km)");
        this.maxFlyDistance = maxFlyDistance;
    }

    @Override
    public Plane createCopy() {
        PassengerPlane plane = new PassengerPlane(this.getMaxAmountOfSeats(), this.getLocation(), this.getSpeed(),
                List.copyOf(this.getRoutes()), this.getHeight(), this.getMaxFlyDistance(), this.getName());
        plane.setId(this.getId());
        return plane;
    }
}
