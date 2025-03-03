package edu.unidrive.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatbotController {

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField userInput;

    // Your Gemini API key and endpoint
    private final String GEMINI_API_KEY = "AIzaSyBDLZW_0M5pC1XHqKO-_n6hELmcbRawJfg";

    // Updated API URL to use v1 instead of v1beta and newer model version
    private final String API_URL = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-pro:generateContent?key=" + GEMINI_API_KEY;

    @FXML
    private void sendMessage() {
        String userMessage = userInput.getText().trim();
        if (userMessage.isEmpty()) {
            return;
        }

        // Display user input in chat area
        chatArea.appendText("You: " + userMessage + "\n");

        // Get response from the bot API
        try {
            String botResponse = getGeminiResponse(userMessage);
            chatArea.appendText("Bot: " + botResponse + "\n");
        } catch (Exception e) {
            // Show error message if API call fails
            chatArea.appendText("Bot: Error fetching response. Please try again.\n");
            System.err.println("Detailed error: " + e.getMessage());
            e.printStackTrace();
        }

        // Clear input field after sending message
        userInput.clear();
    }

    // Function to call Gemini API and fetch response
    private String getGeminiResponse(String prompt) throws Exception {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Create the JSON payload for the request - Updated to match newer API structure
            String jsonPayload = "{ \"contents\": [ { \"parts\": [ { \"text\": \"" + escapeJson(prompt) + "\" } ] } ] }";
            System.out.println("Request payload: " + jsonPayload);

            // Send request body to Gemini API
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Check response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response code: " + responseCode);

            // Read the response from the API (handle both success and error streams)
            String responseBody;
            if (responseCode >= 200 && responseCode < 300) {
                responseBody = readStream(connection.getInputStream());
            } else {
                responseBody = readStream(connection.getErrorStream());
                System.err.println("API Error Response: " + responseBody);
                return "API returned error code: " + responseCode + ". Please check logs for details.";
            }

            System.out.println("Raw Response: " + responseBody);

            // Try to parse the JSON response
            try {
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                System.out.println("Successfully parsed JSON response");

                // Check for error in response
                if (jsonResponse.has("error")) {
                    JsonObject error = jsonResponse.getAsJsonObject("error");
                    String errorMessage = error.has("message") ? error.get("message").getAsString() : "Unknown error";
                    int errorCode = error.has("code") ? error.get("code").getAsInt() : -1;
                    throw new Exception("API Error - Code: " + errorCode + ", Message: " + errorMessage);
                }

                // Try to extract the text response
                if (jsonResponse.has("candidates") && jsonResponse.getAsJsonArray("candidates").size() > 0) {
                    JsonObject candidate = jsonResponse.getAsJsonArray("candidates").get(0).getAsJsonObject();

                    if (candidate.has("content") &&
                            candidate.getAsJsonObject("content").has("parts") &&
                            candidate.getAsJsonObject("content").getAsJsonArray("parts").size() > 0) {

                        return candidate.getAsJsonObject("content")
                                .getAsJsonArray("parts")
                                .get(0)
                                .getAsJsonObject()
                                .get("text")
                                .getAsString();
                    } else {
                        throw new Exception("Unexpected response structure in candidate content. See logs for details.");
                    }
                } else {
                    throw new Exception("Unexpected response format: No candidates found. See logs for details.");
                }
            } catch (JsonSyntaxException e) {
                System.err.println("JSON parsing error: " + e.getMessage());
                throw new Exception("Failed to parse API response as JSON: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Network error: " + e.getMessage());
            throw new Exception("Network error communicating with Gemini API: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    // Helper method to properly escape JSON text
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Helper method to read from InputStream
    private String readStream(java.io.InputStream stream) throws IOException {
        try (Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8.name())) {
            return scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
        }
    }
}