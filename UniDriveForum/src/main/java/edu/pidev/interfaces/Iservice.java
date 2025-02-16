package edu.pidev.interfaces;

import java.util.List;

public interface Iservice<T> {
    public void addEntity(T entity);
    public void removeEntity(T entity) ;
    public void updateEntity(T entity) ;
    public List<T> getAllData();
}
