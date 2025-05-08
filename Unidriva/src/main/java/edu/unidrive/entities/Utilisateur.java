package edu.unidrive.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilisateur {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_PASSAGER = "ROLE_PASSAGER";
    public static final String ROLE_CONDUCTEUR = "ROLE_CONDUCTEUR";

    private int id;
    private String email;
    private LocalDate dob;
    private String gender;
    private String lastname;
    private String firstname;
    private String password;
    private List<String> role = new ArrayList<>(Collections.singletonList(ROLE_USER));
    private String street;
    private String governorate;
    private String phoneNumber;
    private String imageUrl;
    private Integer imageSize;
    private String aboutMe;
    private boolean isActive = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
    private String resetToken;
    private LocalDateTime resetTokenExpiresAt;
    private boolean isBanned = false;
    private LocalDateTime banEndDate;

    // Relationships
    private List<Trajet> trajets = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public Utilisateur() {
        this.createdAt = LocalDateTime.now();
    }

    public Utilisateur(String email, String lastname, String firstname, String password) {
        this();
        this.email = email;
        this.lastname = lastname;
        this.firstname = firstname;
        this.password = password;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        if (role == null || role.isEmpty()) {
            return Collections.singletonList(ROLE_USER);
        }
        return role;
    }

    public void setRoles(List<String> role) {
        this.role = role;
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getGovernorate() {
        return governorate;
    }

    public void setGovernorate(String governorate) {
        this.governorate = governorate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getImageSize() {
        return imageSize;
    }

    public void setImageSize(Integer imageSize) {
        this.imageSize = imageSize;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getResetTokenExpiresAt() {
        return resetTokenExpiresAt;
    }

    public void setResetTokenExpiresAt(LocalDateTime resetTokenExpiresAt) {
        this.resetTokenExpiresAt = resetTokenExpiresAt;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public LocalDateTime getBanEndDate() {
        return banEndDate;
    }

    public void setBanEndDate(LocalDateTime banEndDate) {
        this.banEndDate = banEndDate;
    }



    public List<Trajet> getTrajets() {
        return trajets;
    }

    public void setTrajets(List<Trajet> trajets) {
        this.trajets = trajets;
    }





    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }


    // Business methods
    public void ban(LocalDateTime banEndDate) {
        this.isBanned = true;
        this.banEndDate = banEndDate;
    }

    public void unban() {
        this.isBanned = false;
        this.banEndDate = null;
    }

    public String getBanStatus() {
        if (!isBanned) {
            return "not_banned";
        }
        if (banEndDate == null) {
            return "permanent";
        }
        return banEndDate.isAfter(LocalDateTime.now()) ? "temporary" : "expired";
    }

    public boolean isBanExpired() {
        return isBanned && banEndDate != null && banEndDate.isBefore(LocalDateTime.now());
    }

    public boolean isCurrentlyBanned() {
        return isBanned && (banEndDate == null || banEndDate.isAfter(LocalDateTime.now()));
    }

    public int getProfileCompletionPercentage() {
        List<String> requiredFields = Arrays.asList(
                "email", "lastname", "firstname", // Always filled
                "imageUrl", // Profile picture
                "phoneNumber", // Phone
                "gender", // Gender
                "street", "governorate", // Address
                "aboutMe" // Description
        );

        int completed = 0;

        for (String field : requiredFields) {
            try {
                Object value = this.getClass().getMethod("get" + field.substring(0, 1).toUpperCase() + field.substring(1))
                        .invoke(this);
                if (value != null && !value.toString().isEmpty()) {
                    completed++;
                }
            } catch (Exception e) {
                // Skip if getter doesn't exist
            }
        }

        int totalFields = requiredFields.size();
        int percentage = (int) Math.round((completed / (double) totalFields) * 100);

        return Math.max(20, percentage); // Minimum 20% as some fields are required
    }

    public Map<String, String> getMissingProfileFields() {
        Map<String, String> fields = new HashMap<>();
        fields.put("phoneNumber", "Phone Number");
        fields.put("gender", "Gender");
        fields.put("street", "Street Address");
        fields.put("governorate", "Governorate");
        fields.put("aboutMe", "About me");

        Map<String, String> missing = new HashMap<>();

        for (Map.Entry<String, String> entry : fields.entrySet()) {
            try {
                Object value = this.getClass().getMethod("get" + entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1))
                        .invoke(this);
                if (value == null || value.toString().isEmpty()) {
                    missing.put(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                // Skip if getter doesn't exist
            }
        }

        if (imageUrl == null || imageUrl.isEmpty()) {
            missing.put("imageUrl", "Profile Picture");
        }

        return missing;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", roles=" + role +
                ", isActive=" + isActive +
                '}';
    }
}