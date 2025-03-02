package edu.unidrive.controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PaymentController {

    // Set your Stripe secret key here
    static {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY");  // Load Stripe secret key securely
    }

    @PostMapping("/create-payment-intent")
    public Map<String, String> createPaymentIntent(@RequestBody PaymentRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            // Create the PaymentIntent with the PaymentMethod ID (not raw card data)
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (request.getAmount() * 100))  // Convert amount to cents
                    .setCurrency("eur")
                    .setPaymentMethod(request.getPaymentMethodId())  // Use the PaymentMethod ID, not card data
                    .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                    .setConfirm(true)
                    .build();

            // Create PaymentIntent on Stripe
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Send back clientSecret and PaymentIntent ID for confirmation
            response.put("clientSecret", paymentIntent.getClientSecret());
            response.put("paymentIntentId", paymentIntent.getId());

            return response;
        } catch (StripeException e) {
            // Handle specific Stripe exceptions for better clarity
            e.printStackTrace();
            response.put("error", "Stripe error: " + e.getMessage());
            return response;
        } catch (Exception e) {
            // Handle other errors
            e.printStackTrace();
            response.put("error", "Unexpected error: " + e.getMessage());
            return response;
        }
    }

    // PaymentRequest class to handle the incoming request body
    public static class PaymentRequest {
        private String paymentMethodId;  // This is the PaymentMethod ID from Stripe Elements (not the raw card number)
        private double amount;  // Amount in the base currency (e.g., EUR)

        // Getters and Setters
        public String getPaymentMethodId() {
            return paymentMethodId;
        }

        public void setPaymentMethodId(String paymentMethodId) {
            this.paymentMethodId = paymentMethodId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
}
