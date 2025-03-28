package app.API;

import app.models.PlaneDb;
import app.models.Route;
import app.services.LocationService;
import app.services.interfaces.IFileService;
import app.services.interfaces.ILocationService;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class LocationAPI extends NanoHTTPD {
    private final IFileService fileService;

    public LocationAPI(int port, IFileService fileService) throws IOException {
        super(port);
        this.fileService = fileService;
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("Server running at: http://localhost: " + port);
    }

    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        if ("/get-locations".equals(session.getUri())) {
            String locationsTxt = fileService.readFromFile("locations.json");
            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", locationsTxt);
        }
        return newFixedLengthResponse("Welcome to JFrame HTTP Server");
    }
}
