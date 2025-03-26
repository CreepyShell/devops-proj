package app.windows;

import app.models.User;
import app.services.interfaces.IAuthenticationService;
import app.services.interfaces.ILocationService;
import app.services.interfaces.IRouteService;
import app.services.interfaces.ITicketService;

public class WindowsManager {
    private static WindowsManager windowsManagerInstance = null;
    private User currentUser;
    private final ITicketService ticketService;
    private final IRouteService routeService;
    private final ILocationService locationService;
    private final IAuthenticationService authenticationService;
    private IntroductionWindow introductionWindow;
    private AuthWindow authWindow;
    private MainMenu mainMenu;
    private TicketsWindow ticketsWindow;
    private RoutesWindow routesWindow;


    private WindowsManager(User user, ITicketService ticketService, IRouteService routeService, ILocationService locationService, IAuthenticationService authenticationService) {
        currentUser = user;
        this.ticketService = ticketService;
        this.routeService = routeService;
        this.locationService = locationService;
        this.authenticationService = authenticationService;
    }

    public static WindowsManager getInstance(User user, ITicketService ticketService, IRouteService routeService, ILocationService locationService, IAuthenticationService authenticationService) {
        if (windowsManagerInstance == null)
            windowsManagerInstance = new WindowsManager(user, ticketService, routeService, locationService, authenticationService);
        return windowsManagerInstance;
    }

    public void openIntroductionWindow() {
        introductionWindow = new IntroductionWindow(this);
    }

    public void openAuthWindowLogin() {
        authWindow = new AuthWindow(true, this, authenticationService);
    }

    public void openAuthWindowRegister() {
        authWindow = new AuthWindow(false, this, authenticationService);
    }

    public void openMainMenuWindow(User currentUser) {
        mainMenu = new MainMenu(ticketService, currentUser, this);
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public void openTicketsWindows(User currentUser) {
        ticketsWindow = new TicketsWindow(ticketService, routeService, locationService, currentUser, this);
    }

    public void openRouteWindow() {
        routesWindow = new RoutesWindow(routeService, locationService, this, currentUser);
    }
}
