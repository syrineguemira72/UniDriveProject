package edu.unidrive.services;

import edu.unidrive.interfaces.IDistanceService;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OpenRouteServiceClient implements IDistanceService {

    private static final String API_KEY = "5b3ce3597851110001cf624807b3c63d3a4c42629d0d4e1f61de4571";

    //private static final String API_KEY = System.getenv("OPENROUTE_API_KEY"); // Use environment variable for API key

    @Override
    public double[] getDistanceAndDuration(double lat1, double lon1, double lat2, double lon2) throws IOException {
        String urlString = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=" + API_KEY +
                "&start=" + lon1 + "," + lat1 + "&end=" + lon2 + "," + lat2;

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(url.openStream())) {
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
        }

        // Print the raw JSON response for debugging
        System.out.println("API Response: " + response.toString());

        JSONObject jsonResponse = new JSONObject(response.toString());

        // Check if the response contains an error
        if (jsonResponse.has("error")) {
            throw new IOException("API Error: " + jsonResponse.getJSONObject("error").getString("message"));
        }

        // Parse the new JSON structure
        JSONArray features = jsonResponse.getJSONArray("features");
        if (features.length() == 0) {
            throw new IOException("No route found for the given coordinates.");
        }

        JSONObject firstFeature = features.getJSONObject(0);
        JSONObject properties = firstFeature.getJSONObject("properties");
        JSONArray segments = properties.getJSONArray("segments");

        if (segments.length() == 0) {
            throw new IOException("No segments found in the route.");
        }

        JSONObject firstSegment = segments.getJSONObject(0);
        double distance = firstSegment.getDouble("distance") / 1000; // Convert meters to km
        double duration = firstSegment.getDouble("duration") / 60; // Convert seconds to minutes

        return new double[]{distance, duration};
    }

    @Override
    public List<String> getAutocompleteSuggestions(String input) throws IOException {
        String encodedInput = URLEncoder.encode(input, "UTF-8");
        String urlString = "https://api.openrouteservice.org/geocode/autocomplete?api_key=" + API_KEY + "&text=" + encodedInput;
        URL url = new URL(urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(url.openStream())) {
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
        }

        // Parse the JSON response
        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray features = jsonResponse.getJSONArray("features");

        // Extract location names from the features
        List<String> suggestions = new ArrayList<>();
        for (int i = 0; i < features.length(); i++) {
            JSONObject feature = features.getJSONObject(i);
            String name = feature.getJSONObject("properties").getString("label");
            suggestions.add(name);
        }

        return suggestions;
    }

    @Override
    public double[] getCoordinates(String cityName) throws IOException {
        String encodedCityName = URLEncoder.encode(cityName, "UTF-8");
        String urlString = "https://api.openrouteservice.org/geocode/search?api_key=" + API_KEY + "&text=" + encodedCityName;
        URL url = new URL(urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(url.openStream())) {
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
        }

        // Debug: Print the API response
        System.out.println("API Response for " + cityName + ": " + response.toString());

        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONArray features = jsonResponse.getJSONArray("features");

        if (features.length() == 0) {
            return null; // Location not recognized
        }

        JSONObject firstFeature = features.getJSONObject(0);
        JSONArray coordinates = firstFeature.getJSONObject("geometry").getJSONArray("coordinates");
        return new double[]{coordinates.getDouble(1), coordinates.getDouble(0)}; // [latitude, longitude]
    }
}