package proj.models;

import java.util.UUID;

public class Location {
    private String id;
    private double latitude;
    private double longitude;
    private String city;
    private String country;

    public Location(double latitude, double longitude, String city, String country) {
       setId();
       setLatitude(latitude);
       setLongitude(longitude);
       setCity(city);
       setCountry(country);
    }

    public Location() {
        setId();
        setLongitude(0);
        setLatitude(0);
        setCity("null");
        setCountry("null");
    }

    public String getId() {
        return id;
    }

    private void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    @Override
    public String toString() {
        return "Location: {" +
                "id: " + id +
                ", latitude: " + latitude +
                ", longitude: " + longitude +
                ", city: " + city +
                ", country: " + country +
                '}';
    }
}
