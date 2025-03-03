package edu.unidrive.tests;

import edu.unidrive.services.OpenRouteServiceClient;

import java.io.IOException;

public class OpenRouteServiceClientTest {
    public static void main(String[] args) {
        OpenRouteServiceClient client = new OpenRouteServiceClient();

        try {
            // Test getCoordinates
            String cityName = "Paris";
            double[] coordinates = client.getCoordinates(cityName);
            System.out.println("Coordinates for " + cityName + ": Latitude = " + coordinates[0] + ", Longitude = " + coordinates[1]);

            // Test getDistanceAndDuration
            double lat1 = coordinates[0];
            double lon1 = coordinates[1];
            double[] distanceAndDuration = client.getDistanceAndDuration(lat1, lon1, 48.8566, 2.3522); // Paris to Paris (should be 0 km)
            System.out.println("Distance: " + distanceAndDuration[0] + " km, Duration: " + distanceAndDuration[1] + " minutes");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}