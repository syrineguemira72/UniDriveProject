package edu.unidrive.interfaces;

import edu.unidrive.entities.Event;

import java.util.List;

public interface IEventService {
    void addEvent(Event Event);
    List<Event> getAllEvents();
    Event getEventById(int id);
    void updateEvent(Event Event);
    void deleteEvent(int id);
}
