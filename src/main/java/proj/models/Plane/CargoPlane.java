package proj.models.Plane;

import proj.models.Height;
import proj.models.Location;
import proj.models.Route;

import java.security.InvalidParameterException;
import java.util.List;

public class CargoPlane extends Plane {
    private double maxGoodsWeight;
    private double volume;

    public CargoPlane() {
        super();
        setMaxGoodsWeight(21000);
        setVolume(50);
    }

    public CargoPlane(int maxAmountOfSeats, Location location, double speed, List<Route> routes, Height height, double maxGoodsWeight, double volume, double maxFlyDistance, String name) {
        super(maxAmountOfSeats, location, speed, routes, height, maxFlyDistance, name);
        setMaxGoodsWeight(maxGoodsWeight);
        setVolume(volume);
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        if (volume >= 1500 || volume < 30)
            throw new InvalidParameterException("Volume is too low or too high(can be between 30m3 and 1300m3)");
        this.volume = volume;
    }

    @Override
    public void setMaxAmountOfSeats(int maxAmountOfSeats) {
        if (maxAmountOfSeats >= 10 || maxAmountOfSeats < 0)
            throw new InvalidParameterException("Too many amount of seats in cargo plane or its value less than zero");
        this.maxAmountOfSeats = maxAmountOfSeats;
    }

    @Override
    public void setSpeed(double speed) {
        if (speed >= 262.5 || speed < 0)
            throw new InvalidParameterException("Cargo plane too fast or its speed less than zero");
        this.speed = speed;
    }

    @Override
    public void setMaxFlyDistance(double maxFlyDistance) {
        if (maxFlyDistance >= 20000000 || maxFlyDistance < 20000)
            throw new InvalidParameterException("Max fly distance of a cargo plane is too long or too short(can be between 20km and 15 400km)");
        this.maxFlyDistance = maxFlyDistance;
    }

    public double getMaxGoodsWeight() {
        return this.maxGoodsWeight;
    }

    public void setMaxGoodsWeight(double maxGoodsWeight) {
        if (maxGoodsWeight >= 300000 || maxGoodsWeight < 20000) {
            throw new InvalidParameterException("Max goods weight of a cargo plane is too big or too small(can be between 20000 kg and 250000 kg)");
        }
        this.maxGoodsWeight = maxGoodsWeight;
    }

    @Override
    public Plane createCopy() {
        CargoPlane plane = new CargoPlane(this.getMaxAmountOfSeats(), this.getLocation(), this.getSpeed(),
                List.copyOf(this.getRoutes()), this.getHeight(), this.getMaxGoodsWeight(), this.getVolume(), this.getMaxFlyDistance(), this.getName());
        plane.setId(this.getId());
        return plane;
    }
}
