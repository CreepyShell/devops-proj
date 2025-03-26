package com.models.Plane;

import com.models.Height;
import com.models.Location;
import com.models.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Plane {
    private String id;
    protected int maxAmountOfSeats;
    private Location location;
    protected double speed;
    protected double maxFlyDistance;
    private List<Route> routes;
    private Height height;
    private String name;

    public Plane() {
        setId("");
        setHeight(Height.height1);
        setRoutes(new ArrayList<>());
        setSpeed(200);
        setMaxAmountOfSeats(2);
        setMaxFlyDistance(100001);
        setName("null");
    }

    public Plane(int maxAmountOfSeats, Location location, double speed, List<Route> routes, Height height, double maxFlyDistance, String name) {
        setName(name);
        setId("");
        setMaxAmountOfSeats(maxAmountOfSeats);
        setLocation(location);
        setSpeed(speed);
        setRoutes(routes);
        setHeight(height);
        setMaxFlyDistance(maxFlyDistance);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id.length() == 0) {
            this.id = UUID.randomUUID().toString();
            return;
        }
        this.id = id;
    }

    public int getMaxAmountOfSeats() {
        return maxAmountOfSeats;
    }

    public abstract void setMaxAmountOfSeats(int maxAmountOfSeats);

    public Location getLocation() {
        return new Location(location.getLatitude(), location.getLongitude(), location.getCity(), location.getCountry());
    }

    public void setLocation(Location location) {
        this.location = new Location(location.getLatitude(), location.getLongitude(), location.getCity(), location.getCountry());
    }

    public double getSpeed() {
        return speed;
    }

    public abstract void setSpeed(double speed);

    public double getMaxFlyDistance() {
        return maxFlyDistance;
    }

    public abstract void setMaxFlyDistance(double maxFlyDistance);

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public Height getHeight() {
        return height;
    }

    public void setHeight(Height height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract Plane createCopy();
}
