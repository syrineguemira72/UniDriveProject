package edu.unidrive.services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private final String username;
    private final String password;

    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void sendWelcomeEmail(String recipientEmail, String firstname) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Remplacez par votre serveur SMTP
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Welcome to Unidrive!");
            message.setText("Dear " + firstname + ",\n\nWelcome to Unidrive! We are excited to have you on board.\n\nBest regards,\nThe Unidrive Team");

            Transport.send(message);

            System.out.println("Welcome email sent successfully to " + recipientEmail);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}