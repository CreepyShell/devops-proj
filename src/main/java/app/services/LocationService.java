package app.services;

import app.models.Location;
import app.models.PlaneDb;
import app.services.interfaces.ILocationService;

import java.util.List;

public class LocationService implements ILocationService {
    private final PlaneDb planeDb;

    public LocationService(PlaneDb db) {
        this.planeDb = db;
    }

    @Override
    public double findDistance(Location start, Location end, double radius) {
        double startPointX = Math.toRadians(start.getLatitude());
        double startPointY = Math.toRadians(start.getLongitude());
        double endPointX = Math.toRadians(end.getLatitude());
        double endPointY = Math.toRadians(end.getLongitude());

        double firstStatement = Math.pow(Math.sin(endPointX / 2 - startPointX / 2), 2);
        double secondStatement = Math.cos(startPointX) * Math.cos(endPointX)
                * Math.pow(Math.sin(endPointY / 2 - startPointY / 2), 2);

        return 2 * radius * Math.asin(Math.sqrt(firstStatement + secondStatement));
    }

    @Override
    public Location findLocationByCity(String city) {
        return this.planeDb.getLocations().stream().filter(l -> l.getCity().equals(city)).findAny().orElse(null);
    }

    @Override
    public Location[] findLocationsByCountry(String country) {
        List<Location> locations = this.planeDb.getLocations().stream().filter(l->l.getCountry().equals(country)).toList();
        return locations.toArray(new Location[0]);
    }
}
