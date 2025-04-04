package app.API;

import app.services.interfaces.IFileService;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

public class LocationAPI extends NanoHTTPD {
    private final IFileService fileService;

    public LocationAPI(int port, IFileService fileService) throws IOException {
        super("0.0.0.0", port);
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
