package edu.unidrive.interfaces;

import edu.unidrive.entities.Activity;

import java.util.List;

public interface IActivityService {
    void addActivity(Activity activity);
    List<Activity> getAllActivities();
    Activity getActivityById(int id);
    void updateActivity(Activity activity);
    void deleteActivity(int id);
}