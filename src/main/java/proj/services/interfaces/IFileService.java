package proj.services.interfaces;

public interface IFileService {
    public String getLocationFile();

    public String getRouteFile();

    public String getTicketFile();

    public String getPlaneFile();

    public String getUserFile();

    boolean writeInFile(String text, String fileName);

    String readFromFile(String fileName);
    boolean isEmptyFile(String fileName);
}
