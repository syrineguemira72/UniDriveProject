package edu.unidrive.services;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class EmailService {

    private final String username;
    private final String password;

    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private String loadHtmlTemplate(String filePath, String firstname) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (inputStream == null) {
                throw new RuntimeException("File not found: " + filePath);
            }
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return content.replace("{{firstname}}", firstname)
                    .replace("{{logoCid}}", "logo123");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load HTML template", e);
        }
    }

    public void sendWelcomeEmail(String recipientEmail, String firstname) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
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

            String htmlContent = loadHtmlTemplate("html/emailUser.html", firstname);

            MimeMultipart multipart = new MimeMultipart("related");

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(htmlContent, "text/html");
            multipart.addBodyPart(messageBodyPart);

            MimeBodyPart imagePart = new MimeBodyPart();
            DataSource fds = new FileDataSource("src/main/resources/images/unidrive.png");
            imagePart.setDataHandler(new DataHandler(fds));
            imagePart.setHeader("Content-ID", "<logo123>");
            multipart.addBodyPart(imagePart);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Welcome email sent successfully to " + recipientEmail);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
