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

    private static final String API_URL_INTENTS      = "https://api.stripe.com/v1/payment_intents";
    private static final String API_URL_PAYMENT_METHOD = "https://api.stripe.com/v1/payment_methods";
    private static final String STRIPE_SECRET_KEY    = System.getenv("STRIPE_SECRET_KEY");
    // ← better to store your key in an env var

    /**
     * Controller calls this. Returns true if Stripe reports the payment as succeeded.
     */
    public boolean payerAvecStripe(double montant, String paymentMethodToken) {
        try {
            // Amount → cents
            int amountInCents = convertToCents(Double.toString(montant));

            // Create or confirm a PaymentIntent
            String body = encodeParams(Map.of(
                    "amount",         String.valueOf(amountInCents),
                    "currency",       "eur",
                    "payment_method", paymentMethodToken,
                    "confirm",        "true"
            ));

            String response = sendPostRequest(API_URL_INTENTS, body);
            if (response == null) {
                System.err.println("No response from Stripe when creating PaymentIntent.");
                return false;
            }

            // Quick-and-dirty check: look for `"status":"succeeded"` in the JSON
            return response.contains("\"status\":\"succeeded\"");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // (Optional) expose it if you need the raw JSON for debugging:
    public String createPaymentIntent(String paymentMethodToken, String currency, String montant) {
        try {
            int amountInCents = convertToCents(montant);
            String body = encodeParams(Map.of(
                    "amount",         String.valueOf(amountInCents),
                    "currency",       currency.toLowerCase(),
                    "payment_method", paymentMethodToken,
                    "confirm",        "true"
            ));
            return sendPostRequest(API_URL_INTENTS, body);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int convertToCents(String montant) throws Exception {
        try {
            double d = Double.parseDouble(montant);
            return (int) (d * 100);
        } catch (NumberFormatException e) {
            throw new Exception("Invalid montant format.");
        }
    }

    private String sendPostRequest(String apiUrl, String requestBody) throws Exception {
        if (STRIPE_SECRET_KEY == null || STRIPE_SECRET_KEY.isBlank()) {
            throw new IllegalStateException("Stripe API key missing (set STRIPE_SECRET_KEY).");
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(new URI(apiUrl))
                .header("Authorization", "Bearer " + STRIPE_SECRET_KEY)
                .header("Content-Type",  "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() == 200) {
            return resp.body();
        } else {
            System.err.printf("Stripe error (%d): %s%n", resp.statusCode(), resp.body());
            return null;
        }
    }

    private String encodeParams(Map<String, String> params) {
        return params.entrySet()
                .stream()
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8)
                        + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }
}
