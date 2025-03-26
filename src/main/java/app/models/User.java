package app.models;

import app.services.custom_exceptions.BalanceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String password;
    private String salt;
    private String email;
    private double balance;
    private List<Ticket> tickets;

    public User() {
        setId("");
        setTickets(new ArrayList<>());
        setFirstName("null");
        setLastName("null");
        setSalt();
        setBalance(1000);
    }

    public User(String firstName, String lastName, List<Ticket> tickets, String password, String email, double balance) {
        setBalance(balance);
        setId("");
        setTickets(tickets);
        setLastName(lastName);
        setFirstName(firstName);
        setPassword(password);
        setEmail(email);
        setSalt();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHash() {
        return this.password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt() {
        byte[] array = new byte[16];
        Random random = new Random();
        random.nextBytes(array);
        this.salt = new String(array);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        if (id.length() == 0) {
            this.id = UUID.randomUUID().toString();
            return;
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Ticket> getTickets() {
        return List.copyOf(this.tickets);
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = List.copyOf(tickets);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0)
            throw new BalanceException("Balance can not be negative");
        this.balance = balance;
    }
}
