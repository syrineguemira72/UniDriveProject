package edu.unidrive.services;

import edu.unidrive.entities.Event;
import edu.unidrive.interfaces.IEventService;
import edu.unidrive.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventService implements IEventService {
    private Connection cnx;

    public EventService() {
        cnx = MyConnection.getInstance().getCnx();
        if (cnx == null) {
            System.out.println("Error: Database connection is NULL in EventService!");
        } else {
            System.out.println("Database connection established successfully in EventService.");
        }
    }

    @Override
    public void addEvent(Event event) {
        // Updated query to include the activities field
        String query = "INSERT INTO event (titre, description, date, activities) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, event.getTitre());
            ps.setString(2, event.getDescription());
            ps.setDate(3, new java.sql.Date(event.getDate().getTime()));
            ps.setString(4, event.getActivities()); // Add activities parameter

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    event.setId(generatedKeys.getInt(1)); // Set the generated ID for the event
                }
                System.out.println("Event added successfully with ID: " + event.getId());
            }
        } catch (SQLException e) {
            System.out.println("Error adding event: " + e.getMessage());
        }
    }

    @Override
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM event";

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getInt("id"));
                event.setTitre(rs.getString("titre"));
                event.setDescription(rs.getString("description"));
                event.setDate(rs.getDate("date"));
                event.setActivities(rs.getString("activities")); // Also retrieve activities
                events.add(event);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving events: " + e.getMessage());
        }
        return events;
    }

    @Override
    public Event getEventById(int id) {
        Event event = null;
        String query = "SELECT * FROM event WHERE id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                event = new Event();
                event.setId(rs.getInt("id"));
                event.setTitre(rs.getString("titre"));
                event.setDescription(rs.getString("description"));
                event.setDate(rs.getDate("date"));
                event.setActivities(rs.getString("activities")); // Also retrieve activities
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving event by ID: " + e.getMessage());
        }
        return event;
    }

    @Override
    public void updateEvent(Event event) {
        // Updated query to include activities field
        String query = "UPDATE event SET titre = ?, description = ?, date = ?, activities = ? WHERE id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, event.getTitre());
            ps.setString(2, event.getDescription());
            ps.setDate(3, new java.sql.Date(event.getDate().getTime()));
            ps.setString(4, event.getActivities()); // Add activities parameter
            ps.setInt(5, event.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Event updated successfully.");
            } else {
                System.out.println("No event found with ID: " + event.getId());
            }
        } catch (SQLException e) {
            System.out.println("Error updating event: " + e.getMessage());
        }
    }

    @Override
    public void deleteEvent(int id) {
        String query = "DELETE FROM event WHERE id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Event deleted successfully.");
            } else {
                System.out.println("No event found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting event: " + e.getMessage());
        }
    }
}