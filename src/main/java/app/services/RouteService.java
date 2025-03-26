package app.services;

import org.json.JSONException;
import app.models.Location;
import app.models.Plane.Plane;
import app.models.PlaneDb;
import app.models.Route;
import app.services.interfaces.ILocationService;
import app.services.interfaces.IRouteService;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteService implements IRouteService {
    private final PlaneDb db;
    private final ILocationService locationService;

    public RouteService(PlaneDb db, ILocationService locationService) {
        this.db = db;
        this.locationService = locationService;
    }

    @Override
    public boolean addRoute(Route route) {
        if (route == null) throw new InvalidParameterException("route is null");

        List<Route> routes = db.getRoutes();
        //distance between two location of given route
        double distance = this.locationService.findDistance(route.getTakeOffLocation(), route.getLandingLocation(), 6371000);
        double flySeconds = distance / route.getPlane().getSpeed();                                               //25/11/2022 08:47 - 25/11/2022 12:20 - add
                                                                                                                  //25/11/2022 13:03 - 25/11/2022 17:07 - exist
        for (Route r : routes) {
            //Time which other route plane spends to get from one location to another
            double rFlySeconds = this.locationService.findDistance(r.getTakeOffLocation(), r.getLandingLocation(), 6371000) / r.getPlane().getSpeed();

            //need to ensure the plane is available
//            if (r.getPlane().getId().equals(route.getPlane().getId())) {//chosen plane flies other route        //25/11/2022 17:40 - 25/11/2022 21:10 - add
//                //time which plane has to spend to get the chosen take off location from its landing location   //25/11/2022 13:03 - 25/11/2022 17:07 - exist
//                double flySecondsToTakeOffLocation = this.locationService.findDistance(r.getTakeOffLocation(), route.getTakeOffLocation(), 6371000) / route.getPlane().getSpeed();
//                if (r.getTakeOffTime().after(route.getTakeOffTime())) {
//
//                }
//            }

            boolean isAppropriateLandingTime = Math.abs(route.getTakeOffTime().getTime() - r.getTakeOffTime().getTime()) / (1000.0 * 60) > 25;
            boolean isAppropriateTakingOffTime = Math.abs((route.getTakeOffTime().getTime() + flySeconds * 1000.0 - r.getTakeOffTime().getTime() * rFlySeconds * 1000) / (1000.0 * 60)) > 25;
            if (!isAppropriateTakingOffTime || !isAppropriateLandingTime) return false;
        }

        routes.add(route);
        try {
            db.setRoutes(routes);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateRoute(Route route) {
        return false;
    }

    @Override
    public boolean deleteRoute(Route route) {
        return false;
    }

    @Override
    public List<Plane> getAllPlanes() {
        return db.getPlanes();
    }

    @Override
    public List<Route> getAllRoutes() {
        return db.getRoutes();
    }

    @Override
    public Plane getPlaneByName(String name) {
        return this.db.getPlanes().stream().filter(p -> p.getName().equals(name)).findAny().orElse(null);
    }

    @Override
    public Route getRouteById(String id) {
        Route route = db.getRoutes().stream().filter(r -> r.getId().equals(id)).findAny().orElse(null);
        return route;
    }

    @Override
    public List<Route> findBestRoutesByPrice(Location start, Location end) {
        List<Route> routesLocation = this.findRoutesByLocation(start, end);
        for (int i = 0; i < routesLocation.size(); i++) {
            for (int j = 1; j < routesLocation.size(); j++) {
                Route route1 = routesLocation.get(i);
                Route route2 = routesLocation.get(j);
                if (routesLocation.get(j).getCost() < routesLocation.get(i).getCost()) {
                    routesLocation.set(j, route1);
                    routesLocation.set(i, route2);
                }
            }
        }
        return routesLocation;
    }

    @Override
    public List<Route> findBestRoutesByTime(Location start, Location end) {
        List<Route> routesLocation = this.findRoutesByLocation(start, end);
        //sort by time
        return routesLocation;
    }

    @Override
    public List<Route> findRoutesByLocation(Location startLocation, Location endLocation) {
        if (startLocation == null || endLocation == null) {
            return new ArrayList<>();
        }
        return db.getRoutes().stream().filter(r -> Objects.equals(r.getTakeOffLocation().getCity(), startLocation.getCity()) && Objects.equals(r.getLandingLocation().getCity(), endLocation.getCity())).toList();
    }
}
