package app.windows;

import app.models.Location;
import app.models.Route;
import app.models.User;
import app.services.custom_exceptions.BalanceException;
import app.services.custom_exceptions.RouteException;
import app.services.interfaces.ILocationService;
import app.services.interfaces.IRouteService;
import app.services.interfaces.ITicketService;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class TicketsWindow extends JFrame {
    private final ITicketService ticketService;
    private final IRouteService routeService;
    private final ILocationService locationService;
    private final WindowsManager windowsManager;
    private final User currentUser;

    private JComboBox cb;
    private JButton buyTicket;

    public TicketsWindow(ITicketService ticketService, IRouteService routeService, ILocationService locationService, User user, WindowsManager windowsManager) {
        this.windowsManager = windowsManager;
        this.routeService = routeService;
        this.ticketService = ticketService;
        this.currentUser = user;
        this.locationService = locationService;
        this.setTitle("Buy ticket");
        JPanel panel = new JPanel();
        this.setResizable(false);
        panel.setLayout(null);

        JLabel label = new JLabel("Welcome to the funny airlines!");
        label.setBounds(80, 20, 400, 21);
        label.setFont(new Font("Verdana", Font.BOLD, 20));

        JLabel startLabel = new JLabel("Enter take off city");
        startLabel.setBounds(20, 50, 400, 24);
        startLabel.setFont(new Font("Verdana", Font.ITALIC, 18));

        JTextField startCityField = new JTextField(30);
        startCityField.setFont(new Font("Verdana", Font.PLAIN, 20));
        startCityField.setBounds(220, 50, 250, 25);

        JLabel endLabel = new JLabel("Enter landing city");
        endLabel.setBounds(20, 80, 400, 24);
        endLabel.setFont(new Font("Verdana", Font.ITALIC, 18));

        JTextField endCityField = new JTextField(30);
        endCityField.setFont(new Font("Verdana", Font.PLAIN, 20));
        endCityField.setBounds(220, 80, 250, 25);

        JButton findRoutesButton = new JButton("Find routes");
        findRoutesButton.setBounds(60, 120, 180, 30);
        findRoutesButton.setFont(new Font("Times new Roman", Font.PLAIN, 20));
        findRoutesButton.addActionListener(l -> {
            Location location1 = this.locationService.findLocationByCity(startCityField.getText());
            Location location2 = this.locationService.findLocationByCity(endCityField.getText());
            if (location1 == null) {
                JOptionPane.showMessageDialog(findRoutesButton, "Did not find the take off city");
                return;
            }
            if (location2 == null) {
                JOptionPane.showMessageDialog(findRoutesButton, "Did not find the landing city");
                return;
            }
            showAvailableRoutes(location1, location2);
        });

        cb = new JComboBox();
        cb.setBounds(50, 180, 450, 30);
        cb.setVisible(false);

        buyTicket = new JButton("Buy ticket");
        buyTicket.setBounds(130, 220, 180, 35);
        buyTicket.setFont(new Font("Times new Roman", Font.PLAIN, 23));
        buyTicket.addActionListener(l -> {
            try {

                String item = cb.getSelectedItem().toString();
                Route route = routeService.getRouteById(item.split(": ")[4]);
                this.ticketService.buyTicket(currentUser, route);
                JOptionPane.showMessageDialog(null, "Ticket is bought successfully");
            } catch (RouteException ex) {
                JOptionPane.showMessageDialog(buyTicket, "The plane is full: " + ex.getMessage());
            }
            catch (BalanceException ex){
                JOptionPane.showMessageDialog(buyTicket, "Not enough money:(");
            }
        });
        buyTicket.setVisible(false);


        JButton backButton = new JButton("Back");
        backButton.setBounds(240, 300, 100, 28);
        backButton.setFont(new Font("Times new Roman", Font.PLAIN, 25));
        backButton.addActionListener(l -> {
            this.closeWindow();
            windowsManager.openMainMenuWindow(currentUser);
        });

        panel.add(label);
        panel.add(startLabel);
        panel.add(startCityField);
        panel.add(endLabel);
        panel.add(endCityField);
        panel.add(findRoutesButton);
        this.add(cb);
        this.add(buyTicket);
        panel.add(backButton);

        this.add(panel);
        this.setSize(530, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void showAvailableRoutes(Location start, Location end) {
        cb.removeAllItems();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Route[] routes = this.routeService.findRoutesByLocation(start, end).toArray(new Route[0]);
        if (routes.length == 0) {
            JOptionPane.showMessageDialog(this, "Did not find any routes by given locations");
            return;
        }
        for (Route r : routes) {
            cb.addItem("Take off time: " + format.format(r.getTakeOffTime()) + ", cost: " +
                    r.getCost() + ", plane: " + r.getPlane().getName() + ", id: " + r.getId());
        }
        buyTicket.setVisible(true);
        cb.setVisible(true);
    }

    public void closeWindow() {
        this.setVisible(false);
        this.dispose();
    }
}
