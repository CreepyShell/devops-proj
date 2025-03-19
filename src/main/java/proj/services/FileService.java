package proj.services;

import proj.services.interfaces.IFileService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileService implements IFileService {
    private String locationFile;
    private String routeFile;
    private String ticketFile;
    private String planeFile;
    private String userFile;

    public FileService() {
        setLocationFile("locations.json");
        setRouteFile("routes.json");
        setTicketFile("tickets.json");
        setPlaneFile("Planes.json");
        setUserFile("users.json");
    }

    public FileService(String locationFile, String routeFile, String ticketFile, String planeFile, String userFile) {
        setLocationFile(locationFile);
        setRouteFile(routeFile);
        setTicketFile(ticketFile);
        setPlaneFile(planeFile);
        setUserFile(userFile);
    }

    public String getLocationFile() {
        return locationFile;
    }

    private void setLocationFile(String locationFile) {
        this.locationFile = locationFile;
    }

    public String getRouteFile() {
        return routeFile;
    }

    private void setRouteFile(String routeFile) {
        this.routeFile = routeFile;
    }

    public String getTicketFile() {
        return ticketFile;
    }

    private void setTicketFile(String ticketFile) {
        this.ticketFile = ticketFile;
    }

    public String getPlaneFile() {
        return planeFile;
    }

    private void setPlaneFile(String planeFile) {
        this.planeFile = planeFile;
    }

    public String getUserFile() {
        return userFile;
    }

    private void setUserFile(String userFile) {
        this.userFile = userFile;
    }

    @Override
    public boolean writeInFile(String text, String fileName) {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(text);
            file.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String readFromFile(String fileName) {
        try {
            Path filePath = Paths.get(fileName);
            List<String> lines = Files.readAllLines(filePath);
            StringBuilder res = new StringBuilder();
            for (String line : lines) {
                res.append(line + "\n");
            }
            return res.toString();
        } catch (IOException ex) {
            return "\"Error\":\"Error read file\"";
        }
    }

    @Override
    public boolean isEmptyFile(String fileName) {
        File file = new File(fileName);
        return file.length() == 0;
    }
}
