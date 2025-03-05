package edu.unidrive.interfaces;

import java.io.IOException;
import java.util.List;

public interface IDistanceService {
    double[] getCoordinates(String cityName) throws IOException;
    double[] getDistanceAndDuration(double lat1, double lon1, double lat2, double lon2) throws IOException;
    List<String> getAutocompleteSuggestions(String input) throws IOException;
}
