package app;

import app.models.PlaneDb;
import app.models.User;
import app.services.*;
import app.services.interfaces.*;
import app.windows.WindowsManager;

public class DriverClass {

    private static PlaneDb planeDb;
    private static IFileService fileService;
    private static ITicketService ticketService;
    private static IRouteService routeService;
    private static IAuthenticationService authService;
    private static ILocationService locationService;
    private static User currentUser = new User();

    public static void main(String[] args) {
        fileService = new FileService();
        planeDb = PlaneDb.getPlainDb(fileService);
        ticketService = new TicketService(planeDb);
        locationService = new LocationService(planeDb);
        routeService = new RouteService(planeDb, locationService);
        authService = new AuthenticationService(planeDb);
        WindowsManager windowsManager = WindowsManager.getInstance(currentUser, ticketService, routeService, locationService, authService);
        windowsManager.openIntroductionWindow();
    }
}
