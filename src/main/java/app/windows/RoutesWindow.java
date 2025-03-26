package app.windows;

import app.models.Location;
import app.models.Plane.Plane;
import app.models.Route;
import app.models.User;
import app.services.interfaces.ILocationService;
import app.services.interfaces.IRouteService;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class RoutesWindow extends JFrame {
    private final IRouteService routeService;
    private final ILocationService locationService;
    private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final WindowsManager windowsManager;
    private User currentUser;

    public RoutesWindow(IRouteService routeService, ILocationService locationService, WindowsManager windowsManager, User user) {
        this.windowsManager = windowsManager;
        this.locationService = locationService;
        this.routeService = routeService;
        currentUser = user;
        this.setTitle("Manage route");
        JPanel panel = new JPanel();
        this.setResizable(false);
        panel.setLayout(null);

        JLabel label = new JLabel("Add new route");
        label.setBounds(80, 20, 400, 21);
        label.setFont(new Font("Verdana", Font.BOLD, 20));

        JButton backButton = new JButton("Back");
        backButton.setBounds(500, 10, 100, 28);
        backButton.setFont(new Font("Times new Roman", Font.PLAIN, 25));
        backButton.addActionListener(l -> {
            this.closeWindow();
            windowsManager.openMainMenuWindow(currentUser);
        });

        JLabel startLabel = new JLabel("Enter take off city");
        startLabel.setBounds(20, 50, 400, 24);
        startLabel.setFont(new Font("Verdana", Font.ITALIC, 18));

        JTextField startCityField = new JTextField(30);
        startCityField.setFont(new Font("Verdana", Font.PLAIN, 20));
        startCityField.setBounds(220, 50, 250, 25);

        JLabel endLabel = new JLabel("Enter landing city");
        endLabel.setBounds(20, 80, 250, 24);
        endLabel.setFont(new Font("Verdana", Font.ITALIC, 18));

        JTextField endCityField = new JTextField(30);
        endCityField.setFont(new Font("Verdana", Font.PLAIN, 20));
        endCityField.setBounds(220, 80, 250, 25);

        Label startTimeLabel = new Label("Enter take off time");
        startTimeLabel.setFont(new Font("Verdana", Font.ITALIC, 18));
        startTimeLabel.setBounds(20, 130, 200, 24);

        JTextField startTime = new JTextField();
        startTime.setBounds(250, 130, 250, 25);

        JLabel priceLabel = new JLabel("Enter price");
        priceLabel.setFont(new Font("Verdana", Font.ITALIC, 18));
        priceLabel.setBounds(20, 160, 200, 24);

        JTextField priceField = new JTextField();
        priceField.setBounds(250, 160, 250, 25);

        ArrayList<String> planeStr = new ArrayList<>();
        for (Plane p : this.routeService.getAllPlanes()) {
            planeStr.add("Name: " + p.getName() + ", Amount of seats: " + p.getMaxAmountOfSeats() + ", Speed: " + p.getSpeed() + " meters per second");
        }

        JComboBox planeComboBox = new JComboBox(planeStr.toArray());
        planeComboBox.setBounds(30, 200, 450, 30);

        JButton addRoute = new JButton("Add route");
        addRoute.setBounds(60, 270, 180, 30);
        addRoute.setFont(new Font("Times new Roman", Font.PLAIN, 20));
        addRoute.addActionListener(l -> {
            try {
                Location location1 = locationService.findLocationByCity(startCityField.getText());
                Location location2 = locationService.findLocationByCity(endCityField.getText());
                Date date1 = format.parse(startTime.getText());
                double price = Double.parseDouble(priceField.getText());
                Plane plane = routeService.getPlaneByName(planeComboBox.getSelectedItem().toString().split("")[0]);
                Route route = new Route(date1, location1, location2, plane, price);
                routeService.addRoute(route);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(addRoute, "Error parsing:" + e.getMessage());
            }


        });

        JLabel choseRouteLabel = new JLabel("Choose route to delete or update");
        choseRouteLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        choseRouteLabel.setBounds(20, 320, 400, 24);

        ArrayList<String> routeStr = new ArrayList<>();
        for (Route r : this.routeService.getAllRoutes()) {
            routeStr.add(r.toString());
        }

        JComboBox routeComboBox = new JComboBox(routeStr.toArray());
        routeComboBox.setBounds(20, 350, 600, 30);

        Label updateStartTimeLabel = new Label("Update take off time");
        updateStartTimeLabel.setFont(new Font("Verdana", Font.ITALIC, 18));
        updateStartTimeLabel.setBounds(20, 390, 200, 24);

        Route route = this.getRouteByString(Objects.requireNonNull(routeComboBox.getSelectedItem()).toString());
        JTextField updateStartTime = new JTextField(format.format(route.getTakeOffTime()));
        updateStartTime.setBounds(250, 390, 250, 25);

        JLabel updatePriceLabel = new JLabel("Update price");
        updatePriceLabel.setFont(new Font("Verdana", Font.ITALIC, 18));
        updatePriceLabel.setBounds(20, 420, 200, 24);

        JTextField updatePriceField = new JTextField(String.valueOf(route.getCost()));
        updatePriceField.setBounds(250, 420, 150, 25);

        //https://stackoverflow.com/questions/17576446/java-jcombobox-listen-a-change-selection-event
        routeComboBox.addItemListener(i->{
//            Route routeUpdated = getRouteByString(routeComboBox.getSelectedItem().toString());
//            updateStartTime.setText(format.format(routeUpdated.getTakeOffTime()));
//            updatePriceField.setText(String.valueOf(route.getCost()));
        });

        JLabel updatePlaneLabel = new JLabel("Update plane");
        updatePlaneLabel.setFont(new Font("Verdana", Font.ITALIC, 18));
        updatePlaneLabel.setBounds(20, 450, 200, 24);

        ArrayList<String> planeNameStr = new ArrayList<>();
        for (Plane p : this.routeService.getAllPlanes()) {
            planeNameStr.add(p.getName());
        }

        JComboBox planeNameComboBox = new JComboBox(planeNameStr.toArray());
        planeNameComboBox.setBounds(250, 450, 150, 30);

        JButton updateRoute = new JButton("Update route");
        updateRoute.setBounds(60, 500, 150, 30);
        updateRoute.setFont(new Font("Times new Roman", Font.PLAIN, 20));

        JButton deleteRoute = new JButton("Remove route");
        deleteRoute.setBounds(260, 500, 180, 30);
        deleteRoute.setFont(new Font("Times new Roman", Font.PLAIN, 20));

        panel.add(label);
        panel.add(startLabel);
        panel.add(startCityField);
        panel.add(endLabel);
        panel.add(endCityField);
        panel.add(startTimeLabel);
        panel.add(startTime);
        panel.add(priceLabel);
        panel.add(priceField);
        panel.add(planeComboBox);
        panel.add(addRoute);
        panel.add(choseRouteLabel);
        panel.add(backButton);
        panel.add(routeComboBox);
        panel.add(updateStartTimeLabel);
        panel.add(updateStartTime);
        panel.add(updatePriceLabel);
        panel.add(updatePriceField);
        panel.add(updatePlaneLabel);
        panel.add(planeNameComboBox);
        panel.add(updateRoute);
        panel.add(deleteRoute);

        this.add(panel);
        this.setSize(650, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private Route getRouteByString(String str) {
        try {
            String dateStr = str.split(", ")[0].split("=")[1];
            String location1 = str.split(", ")[1].split(":")[1];
            String location2 = str.split(", ")[2].split(":")[1];
            Date date = format.parse(dateStr);
            return this.routeService.findRoutesByLocation
                            (this.locationService.findLocationByCity(location1), this.locationService.findLocationByCity(location2))
                    .stream().filter(r -> r.getTakeOffTime().equals(date)).findAny().orElse(null);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "The date format is incorrect. Ir must be dd/MM/yyyy HH:mm:ss");
            return new Route();
        }
    }

    public void closeWindow() {
        this.setVisible(false);
        this.dispose();
    }
}
