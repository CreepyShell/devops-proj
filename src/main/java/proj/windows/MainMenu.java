package proj.windows;

import proj.models.Ticket;
import proj.models.User;
import proj.services.interfaces.ITicketService;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainMenu extends JFrame {
    private final ITicketService ticketService;
    private final WindowsManager windowsManager;
    private final User currentUser;

    public MainMenu(ITicketService ticketService, User user, WindowsManager windowsManager) {
        this.windowsManager = windowsManager;
        this.ticketService = ticketService;
        this.currentUser = user;
        this.setTitle("Main menu");
        JPanel panel = new JPanel();
        this.setResizable(false);
        panel.setLayout(null);

        JLabel labelName = new JLabel(currentUser.getFirstName() + " " + currentUser.getLastName() + "!");
        labelName.setBounds(30, 20, 450, 26);
        labelName.setFont(new Font("Verdana", Font.PLAIN, 22));

        JLabel label = new JLabel("Welcome to the funny airlines!");
        label.setBounds(80, 60, 400, 21);
        label.setFont(new Font("Verdana", Font.BOLD, 18));

        JLabel labelBalance = new JLabel("Your balance is " + currentUser.getBalance() + "$");
        labelBalance.setBounds(150, 90, 200, 21);
        labelBalance.setFont(new Font("Verdana", Font.ITALIC, 15));
        labelBalance.setBackground(Color.GREEN);

        ArrayList<String> ticketsStr = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        for (Ticket t : this.ticketService.getTicketsByUserId(this.currentUser.getId())) {
            ticketsStr.add("Bought " + format.format(t.getDateBought()) + ", from " + t.getRoute().getTakeOffLocation().getCity() +
                    ", to " + t.getRoute().getLandingLocation().getCity() + " " + t.getRoute().getCost() + "$, " + t.getId());
        }

        JComboBox cb = new JComboBox(ticketsStr.toArray());
        cb.setBounds(50, 120, 400, 30);

        JButton cancelTicket = new JButton("Cancel chosen ticket");
        cancelTicket.setBounds(60, 170, 180, 30);
        cancelTicket.setFont(new Font("Times new Roman", Font.PLAIN, 15));
        cancelTicket.addActionListener(l -> {
            if (cb.getItemCount() == 0) {
                JOptionPane.showMessageDialog(cancelTicket, "You have no tickets");
                return;
            }
            Object selectedItem = cb.getSelectedItem();
            String ticketId = selectedItem.toString().split(", ")[3];
            Ticket ticket = ticketService.getTicketById(ticketId);
            if (ticketService.cancelTicket(currentUser, ticket)) {
                JOptionPane.showMessageDialog(cancelTicket, "Ticket successfully canceled");
                cb.removeItem(selectedItem);
                currentUser.setBalance(currentUser.getBalance() + ticket.getRoute().getCost() * 0.9);
                labelBalance.setText("Your balance is " + currentUser.getBalance() + "$");
                return;
            }
            JOptionPane.showMessageDialog(cancelTicket, "You can not cancel a ticket less than 7 days before take off");
        });

        JButton rescheduleTicket = new JButton("Reschedule chosen ticket");
        rescheduleTicket.setBounds(250, 170, 200, 30);
        rescheduleTicket.setFont(new Font("Times new Roman", Font.PLAIN, 15));

        JButton buyTicket = new JButton("You can buy ticket here");
        buyTicket.setBounds(100, 220, 280, 50);
        buyTicket.setFont(new Font("Times new Roman", Font.PLAIN, 19));
        buyTicket.addActionListener(l -> {
            this.closeWindow();
            windowsManager.openTicketsWindows(currentUser);
        });

        JButton manageRoute = new JButton("Manage routes(only for admin)");
        manageRoute.setBounds(100, 280, 280, 50);
        manageRoute.setFont(new Font("Times new Roman", Font.PLAIN, 19));
        manageRoute.addActionListener(l -> {
            if (!user.getEmail().equals("admin@gmail.com")) {
                JOptionPane.showMessageDialog(manageRoute, "You are not allowed to manage routes");
                return;
            }
            windowsManager.openRouteWindow();
            this.closeWindow();
        });

        JButton logOutButton = new JButton("LogOut");
        logOutButton.setBounds(80, 360, 120, 28);
        logOutButton.setFont(new Font("Times new Roman", Font.PLAIN, 25));
        logOutButton.addActionListener(l -> {
            this.closeWindow();
            this.windowsManager.openIntroductionWindow();
            this.windowsManager.setUser(null);
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(240, 360, 100, 28);
        exitButton.setFont(new Font("Times new Roman", Font.PLAIN, 25));
        exitButton.addActionListener(l -> {
            System.exit(0);
        });

        panel.add(labelName);
        panel.add(label);
        panel.add(labelBalance);
        panel.add(buyTicket);
        panel.add(manageRoute);
        panel.add(cancelTicket);
        panel.add(rescheduleTicket);
        panel.add(exitButton);
        panel.add(logOutButton);

        this.add(cb);
        this.add(panel);
        this.setSize(530, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void closeWindow() {
        this.setVisible(false);
        this.dispose();
    }
}
