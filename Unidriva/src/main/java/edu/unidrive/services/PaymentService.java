package edu.unidrive.services;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class PaymentService {

    private static final String API_URL = "https://api.stripe.com/v1/payment_intents";
    private static final String STRIPE_SECRET_KEY = "sk_test_51Qx9PzQMMc132QGJ0mcQXGv3GNdLd8aULDN3CuHZLLQM58oBsDL4QWcQjZo5F3PwVEeUcc5l82x2QSmWyckQNL7d0028c0fDIu"; // API key should be set securely

    public String createPaymentIntent(String cardNumber, String cvc, String montant) {
        try {
            // Validate API key
            if (STRIPE_SECRET_KEY == null || STRIPE_SECRET_KEY.isEmpty()) {
                throw new IllegalStateException("Stripe API key is missing. Set it as an environment variable.");
            }

            // Set default expiry date to 12/26 if not provided
            String expiry = "12/26"; // Default expiry date

            // Ensure expiry format is valid (MM/YY)
            expiry = expiry.trim(); // Remove any leading/trailing spaces

            if (!expiry.matches("^(0[1-9]|1[0-2])/[0-9]{2}$")) {
                throw new Exception("Invalid expiry date format. Please use MM/YY.");
            }

            // Convert montant (amount) to integer in cents
            int amountInCents = convertToCents(montant);

            // Log montant in cents for debugging
            System.out.println("Amount (in cents) to be charged: " + amountInCents);

            // Step 1: Create a PaymentMethod using card details
            String paymentMethodId = createPaymentMethod(cardNumber, expiry, cvc, montant);
            if (paymentMethodId == null) {
                throw new Exception("Failed to create payment method.");
            }

            // Step 2: Create PaymentIntent
            Map<String, String> params = Map.of(
                    "amount", String.valueOf(amountInCents), // Amount in cents
                    "currency", "usd", // Adjust to the correct currency if necessary
                    "payment_method", paymentMethodId,
                    "confirm", "true"
            );

            // Log the request parameters
            System.out.println("Creating PaymentIntent with the following parameters:");
            System.out.println("Amount: " + amountInCents + " (in cents)");
            System.out.println("Currency: usd");
            System.out.println("Payment Method: " + paymentMethodId);

            String requestBody = encodeParams(params);

            // Log the request body for debugging
            System.out.println("Request Body: " + requestBody);

            // Send request to Stripe API for PaymentIntent creation
            String response = sendPostRequest(API_URL, requestBody);

            if (response != null) {
                // Log the response body for debugging
                System.out.println("Stripe Response: " + response);
                return response; // Return Stripe response
            } else {
                throw new Exception("Payment failed: No response from Stripe API.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    // Convert montant (amount) string to integer in cents (e.g., 100.50 -> 10050)
    private int convertToCents(String montant) throws Exception {
        try {
            double montantValue = Double.parseDouble(montant);
            return (int) (montantValue * 100); // Convert to cents
        } catch (NumberFormatException e) {
            throw new Exception("Invalid montant format. Please provide a valid number.");
        }
    }

    // Create a PaymentMethod using card details
    private String createPaymentMethod(String cardNumber, String expiry, String cvc, String montant) throws Exception {
        String API_URL_PAYMENT_METHOD = "https://api.stripe.com/v1/payment_methods";

        // Extract month and year from expiry date
        String expMonth = expiry.substring(0, 2);  // Extract month (MM)
        String expYear = "20" + expiry.substring(3);  // Extract year (YY) and convert to YYYY

        // Convert montant (amount) to integer in cents
        int amountInCents = convertToCents(montant);

        // Log the amount (for debugging purposes)
        System.out.println("Amount (in cents) to be charged: " + amountInCents);
        System.out.println("Amount (in dollars): " + montant); // Log the original amount in dollars

        Map<String, String> params = Map.of(
                "type", "card",
                "card[number]", cardNumber,
                "card[exp_month]", expMonth,  // MM part
                "card[exp_year]", expYear,  // YYYY part
                "card[cvc]", cvc,
                "amount", String.valueOf(amountInCents) // Adding amount to the parameters
        );

        String requestBody = encodeParams(params);

        // Log the request body for debugging
        System.out.println("Request Body for PaymentMethod creation: " + requestBody);

        // Send request to Stripe API for PaymentMethod creation
        String response = sendPostRequest(API_URL_PAYMENT_METHOD, requestBody);
        if (response != null) {
            return extractPaymentMethodId(response);
        } else {
            throw new Exception("Failed to create payment method.");
        }
    }


    // Sends a POST request to the provided API URL with the given body and returns the response body
    private String sendPostRequest(String apiUrl, String requestBody) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(apiUrl))
                .header("Authorization", "Bearer " + STRIPE_SECRET_KEY)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Log response for debugging
        System.out.println("Response Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            System.out.println("Error: " + response.body());
            return null; // If request fails, return null
        }
    }

    // Extracts the PaymentMethod ID from the JSON response
    private String extractPaymentMethodId(String jsonResponse) {
        int start = jsonResponse.indexOf("\"id\": \"pm_");
        if (start == -1) return null;

        int end = jsonResponse.indexOf("\"", start + 7);
        return jsonResponse.substring(start + 7, end);
    }

    // Helper method to encode URL parameters
    private String encodeParams(Map<String, String> params) {
        return params.entrySet().stream()
                .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }
}
