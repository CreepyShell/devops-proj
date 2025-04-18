package app.API;

import app.services.interfaces.IFileService;
import fi.iki.elonen.NanoHTTPD;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import io.prometheus.client.hotspot.DefaultExports;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class LocationAPI extends NanoHTTPD {
    private final IFileService fileService;

    public LocationAPI(int port, IFileService fileService) throws IOException {
        super("0.0.0.0", port);
        this.fileService = fileService;
        DefaultExports.initialize();
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("Server running at: http://localhost: " + port);
    }

    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        String url = session.getUri();

        if ("/get-locations".equals(url)) {
            String locationsTxt = fileService.readFromFile("locations.json");
            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", locationsTxt);
        }
        if ("/get-routes".equals(url)) {
            String routesTxt = fileService.readFromFile("routes.json");
            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", routesTxt);
        }
        if ("/get-private-plain".equals(url)) {
            String privatePlaneTxt = fileService.readFromFile("privatePlanes.json");
            return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "application/json", privatePlaneTxt);
        }
        if ("/metrics".equals(url)) {
            try {
                Writer writer = new StringWriter();
                TextFormat.write004(writer, CollectorRegistry.defaultRegistry.metricFamilySamples());
                return newFixedLengthResponse(Response.Status.OK, TextFormat.CONTENT_TYPE_004, writer.toString());
            } catch (IOException e) {
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "Error generating metrics");
            }
        }
        return newFixedLengthResponse("<h1>Page not found</h1><br><h3>Please verify the url</h3>");
    }
}
