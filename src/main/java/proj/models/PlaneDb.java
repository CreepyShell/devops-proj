package proj.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import proj.models.Plane.CargoPlane;
import proj.models.Plane.PassengerPlane;
import proj.models.Plane.Plane;
import proj.models.Plane.PrivatePlane;
import proj.services.interfaces.IFileService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlaneDb {
    private static PlaneDb planeDbInstance;
    private List<Plane> planes;
    private List<Route> routes;
    private List<Ticket> tickets;
    private List<User> users;
    private List<Location> locations;

    private final IFileService fileService;
    private final Gson json;

    //https://www.geeksforgeeks.org/singleton-class-java/
    private PlaneDb(IFileService fileService) throws JSONException {
        json = new GsonBuilder().setPrettyPrinting().setDateFormat("dd/MM/yyyy HH:mm:ss").create();
        this.fileService = fileService;
        tickets = new ArrayList<>();
        users = new ArrayList<>();

        if (fileService.isEmptyFile(fileService.getLocationFile())) {
            this.insertValuesIntoLocation();
            writeLocationsInFile();
        } else
            this.readLocationsFromFile();

        if (fileService.isEmptyFile("cargo" + fileService.getPlaneFile()) ||
                fileService.isEmptyFile("passenger" + fileService.getPlaneFile()) ||
                fileService.isEmptyFile("private" + fileService.getPlaneFile())) {
            this.insertValuesIntoPlane();
            writePlanesInFile();
        } else
            this.readPlanesFromFile();

        if (fileService.isEmptyFile(fileService.getRouteFile())) {
            this.insertValuesIntoRoute();
            writeRoutesInFile();
        } else
            this.readRoutesFromFile();

        if (!fileService.isEmptyFile(fileService.getUserFile()))
            this.readUsersFromFile();

        if (!fileService.isEmptyFile(fileService.getTicketFile())) {
            this.readTicketsFromFile();
        }
    }

    public static PlaneDb getPlainDb(IFileService fileService) throws JSONException {
        if (planeDbInstance == null)
            planeDbInstance = new PlaneDb(fileService);
        return planeDbInstance;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
        this.writeLocationsInFile();
    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<Plane> getPlanes() {
        return planes;
    }

    public void setPlanes(List<Plane> planes) {
        this.planes = planes;
        this.writePlanesInFile();
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) throws JSONException {
        this.routes = routes;
        this.writeRoutesInFile();
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
        this.writeTicketsInFile();
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        this.writeUsersInFile();
    }

    public void writeLocationsInFile() {
        String locations = json.toJson(this.locations, new TypeToken<ArrayList<Location>>() {
        }.getType());
        fileService.writeInFile(locations, fileService.getLocationFile());
    }

    public void writeRoutesInFile() throws JSONException {
        JSONArray array = new JSONArray();
        for (Route route : this.getRoutes()) {
            JSONObject object = new JSONObject(route);
            if (route.getPlane().getClass().getSimpleName().equals("PassengerPlane")) {
                object.put("passengerPlane", new JSONObject(json.toJson(route.getPlane())));
                object.remove("plane");
                array.put(object);
            }
            if (route.getPlane().getClass().getSimpleName().equals("CargoPlane")) {
                object.put("cargoPlane", new JSONObject(json.toJson(route.getPlane())));
                object.remove("plane");
                array.put(object);
            }
            if (route.getPlane().getClass().getSimpleName().equals("PrivatePlane")) {
                object.put("privatePlane", new JSONObject(json.toJson(route.getPlane())));
                object.remove("plane");
                array.put(object);
            }
            object.remove("takeOffTime");
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            object.put("takeOffTime", format.format(route.getTakeOffTime()));
        }
        fileService.writeInFile(array.toString(4), fileService.getRouteFile());
    }


    public void writePlanesInFile() {
        List<PrivatePlane> privatePlanes = new ArrayList<>();
        List<CargoPlane> cargoPlanes = new ArrayList<>();
        List<PassengerPlane> passengerPlanes = new ArrayList<>();
        for (Plane plane : this.getPlanes()) {
            if (plane.getClass().getSimpleName().equals("PrivatePlane"))
                privatePlanes.add((PrivatePlane) plane);
            if (plane.getClass().getSimpleName().equals("PassengerPlane"))
                passengerPlanes.add((PassengerPlane) plane);
            if (plane.getClass().getSimpleName().equals("CargoPlane"))
                cargoPlanes.add((CargoPlane) plane);
        }
        String passPlanes = json.toJson(passengerPlanes, new TypeToken<ArrayList<PassengerPlane>>() {
        }.getType());
        fileService.writeInFile(passPlanes, "passenger" + fileService.getPlaneFile());

        String carPlanes = json.toJson(cargoPlanes, new TypeToken<ArrayList<CargoPlane>>() {
        }.getType());
        fileService.writeInFile(carPlanes, "cargo" + fileService.getPlaneFile());

        String planesPrivate = json.toJson(privatePlanes, new TypeToken<ArrayList<PrivatePlane>>() {
        }.getType());
        fileService.writeInFile(planesPrivate, "private" + fileService.getPlaneFile());
    }

    public void writeUsersInFile() {
        String users = json.toJson(this.users, new TypeToken<ArrayList<User>>() {
        }.getType());
        fileService.writeInFile(users, fileService.getUserFile());
    }

    public void writeTicketsInFile() {
        for (Ticket ticket : this.tickets)
            ticket.getRoute().setPlane(null);
        String tickets = json.toJson(this.tickets, new TypeToken<ArrayList<Ticket>>() {
        }.getType());
        fileService.writeInFile(tickets, fileService.getTicketFile());
    }

    public void readLocationsFromFile() {
        String resJson = fileService.readFromFile(fileService.getLocationFile());
        this.locations = new ArrayList<>(Arrays.asList(json.fromJson(resJson, Location[].class)));
    }

    public void readRoutesFromFile() throws JSONException {
        String resJson = fileService.readFromFile(fileService.getRouteFile());
        JSONArray array = new JSONArray(resJson);
        this.routes = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject currentObj = array.getJSONObject(i);
            Plane routePlane = null;
            if (!currentObj.isNull("passengerPlane")) {
                routePlane = json.fromJson(currentObj.getString("passengerPlane"), PassengerPlane.class);
                currentObj.remove("passengerPlane");
            }
            if (!currentObj.isNull("privatePlane")) {
                routePlane = json.fromJson(currentObj.getString("privatePlane"), PrivatePlane.class);
                currentObj.remove("privatePlane");
            }
            if (!currentObj.isNull("cargoPlane")) {
                routePlane = json.fromJson(currentObj.getString("cargoPlane"), CargoPlane.class);
                currentObj.remove("cargoPlane");
            }
            Route route = json.fromJson(currentObj.toString(), Route.class);
            route.setPlane(routePlane);
            this.routes.add(route);
        }
    }

    public void readPlanesFromFile() {
        this.planes = new ArrayList<>();
        this.planes.addAll(Arrays.asList(json.fromJson(readAddTypeOfPlanes("passenger"), PassengerPlane[].class)));
        this.planes.addAll(Arrays.asList(json.fromJson(readAddTypeOfPlanes("private"), PrivatePlane[].class)));
        this.planes.addAll(Arrays.asList(json.fromJson(readAddTypeOfPlanes("cargo"), CargoPlane[].class)));
    }

    private String readAddTypeOfPlanes(String planeType) {
        return fileService.readFromFile(planeType + fileService.getPlaneFile());
    }

    public void readUsersFromFile() {
        String resJson = fileService.readFromFile(fileService.getUserFile());
        this.users = new ArrayList<>(Arrays.asList(json.fromJson(resJson, User[].class)));
    }

    public void readTicketsFromFile() {
        String resJson = fileService.readFromFile(fileService.getTicketFile());
        this.tickets = new ArrayList<>(Arrays.asList(json.fromJson(resJson, Ticket[].class)));
    }

    public void insertValuesIntoLocation() {
        Location Dublin = new Location(53.34913137291644, -6.2533176466138745, "Dublin", "Ireland");
        Location Manchester = new Location(53.497779723632654, -2.2673056791182837, "Manchester", "England");
        Location London = new Location(51.690625780845124, -0.11788861617249717, "London", "England");
        Location Brussels = new Location(53.34913137291644, -6.2533176466138745, "Brussels", "Belgium");
        Location Paris = new Location(50.865721803374974, -4.350009652499109, "Paris", "France");
        Location Berlin = new Location(52.61380999259219, -13.531609949890269, "Berlin", "Germany");
        Location Prague = new Location(50.274137039801815, -14.305031144146227, "Prague", "Czech Republic");
        Location Vienna = new Location(48.28293432098631, -16.33993460476889, "Vienna", "Austria");
        Location Krakow = new Location(50.06507482883401, -19.903763551519898, "Krakow", "Poland");
        Location Sarajevo = new Location(43.91538753360366, -18.355641326316093, "Sarajevo", "Bosnia and Herzegovina");
        Location Madrid = new Location(40.49063344791089, -3.6572620651201047, "Madrid", "Spain");
        Location Vilnius = new Location(54.72683764873091, 25.273852330480903, "Vilnius", "Lithuania");
        Location Riga = new Location(57.00008093102882, 24.116019568341898, "Riga", "Latvia");
        Location Oslo = new Location(59.935575723255056, 10.7312159715655, "Oslo", "Norway");
        Location Stockholm = new Location(59.4695190465069, 18.325150625144047, "Stockholm", "Sweden");
        Location Reykjavik = new Location(64.1793281274622, -21.94031583207245, "Reykjavik", "Iceland");
        Location Rome = new Location(41.94306279994936, 12.72949938098774, "Rome", "Italy");
        Location Lisbon = new Location(38.709999444764044, -9.100306821892504, "Lisbon", "Portugal");
        Location Uzhhorod = new Location(48.642650142875674, 22.288297722514283, "Uzhhorod", "Ukraine");
        Location Zagreb = new Location(45.828760669850595, 16.00426562579662, "Zagreb", "Croatia");

        this.locations = new ArrayList<>() {{
            add(Dublin);
            add(Manchester);
            add(London);
            add(Brussels);
            add(Paris);
            add(Berlin);
            add(Prague);
            add(Vienna);
            add(Krakow);
            add(Sarajevo);
            add(Madrid);
            add(Vilnius);
            add(Riga);
            add(Oslo);
            add(Stockholm);
            add(Reykjavik);
            add(Rome);
            add(Lisbon);
            add(Uzhhorod);
            add(Zagreb);
        }};
    }//20 locations

    public void insertValuesIntoPlane() {
        Plane A220_100 = new PassengerPlane(110, this.getLocations().get(1), 240, new ArrayList<>(), Height.height6, 4074000, "A220_100");
        Plane Airbus_A330 = new PassengerPlane(257, this.getLocations().get(15), 281, new ArrayList<>(), Height.height10, 14390000, "Airbus_A330");
        Plane Boeing_747 = new PassengerPlane(400, this.getLocations().get(17), 290, new ArrayList<>(), Height.height7, 8560000, "Boeing_747");
        Plane Stinson_v_77 = new PrivatePlane(4, this.locations.get(9), 100, new ArrayList<>(), Height.height1, 1368000, "Stinson_v_77");
        Plane GulfStream_G700 = new PrivatePlane(19, this.locations.get(2), 300, new ArrayList<>(), Height.height2, 13890000, "GulfStream_G700");
        Plane An_225Mrija = new CargoPlane(3, this.locations.get(8), 250, new ArrayList<>(), Height.height2, 250000.0, 1300.0, 15400000, "An_225Mrija");
        Plane Boeing_777F = new CargoPlane(2, this.locations.get(5), 230, new ArrayList<>(), Height.height8, 103000, 653, 9070000, "Boeing_777F");

        this.planes = new ArrayList<>() {{
            add(A220_100);
            add(Airbus_A330);
            add(Boeing_747);
            add(Stinson_v_77);
            add(GulfStream_G700);
            add(An_225Mrija);
            add(Boeing_777F);
        }};

    }//7 planes:3 pass, 2 private and 2 cargo

    public void insertValuesIntoRoute() {
        Route route1 = new Route(new GregorianCalendar(2023, Calendar.FEBRUARY, 11, 23, 12).getTime(),
                this.getLocations().get(1), this.getLocations().get(2), this.getPlanes().get(0), 190);

        Route route2 = new Route(new GregorianCalendar(2022, Calendar.JANUARY, 5, 9, 36).getTime(),
                this.getLocations().get(13), this.getLocations().get(18), this.getPlanes().get(2), 200);

        Route route3 = new Route(new GregorianCalendar(2022, Calendar.JANUARY, 27, 22, 56).getTime(),
                this.getLocations().get(17), this.getLocations().get(1), this.getPlanes().get(4), 190);

        Route route4 = new Route(new GregorianCalendar(2023, Calendar.JULY, 12, 6, 46).getTime(),
                this.getLocations().get(8), this.getLocations().get(15), this.getPlanes().get(3), 190);
        Route route5 = new Route(new GregorianCalendar(2022, Calendar.DECEMBER, 16, 8, 7).getTime(),
                this.getLocations().get(5), this.getLocations().get(16), this.getPlanes().get(0), 190);

        Route route6 = new Route(new GregorianCalendar(2023, Calendar.JULY, 4, 3, 25).getTime(),
                this.getLocations().get(10), this.getLocations().get(4), this.getPlanes().get(2), 190);

        Route route7 = new Route(new GregorianCalendar(2023, Calendar.MAY, 1, 14, 44).getTime(),
                this.getLocations().get(13), this.getLocations().get(2), this.getPlanes().get(4), 190);

        Route route8 = new Route(new GregorianCalendar(2023, Calendar.AUGUST, 3, 22, 14).getTime(),
                this.getLocations().get(3), this.getLocations().get(17), this.getPlanes().get(2), 190);

        Route route9 = new Route(new GregorianCalendar(2023, Calendar.FEBRUARY, 25, 2, 25).getTime(),
                this.getLocations().get(5), this.getLocations().get(0), this.getPlanes().get(1), 190);

        Route route10 = new Route(new GregorianCalendar(2022, Calendar.DECEMBER, 29, 17, 38).getTime(),
                this.getLocations().get(6), this.getLocations().get(11), this.getPlanes().get(2), 190);

        this.routes = new ArrayList<>() {{
            add(route1);
            add(route2);
            add(route3);
            add(route4);
            add(route5);
            add(route6);
            add(route7);
            add(route8);
            add(route9);
            add(route10);
        }};
    }//10 routes
}