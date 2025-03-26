package com.services.interfaces;

import com.models.Location;

public interface ILocationService {
    double findDistance(Location start, Location end, double radius);
    Location findLocationByCity(String city);
    Location[] findLocationsByCountry(String country);
}
