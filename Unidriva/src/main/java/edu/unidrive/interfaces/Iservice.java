package edu.unidrive.interfaces;

import java.util.List;

public interface Iservice<T> {
    public void add(T entity);
    public void remove(T entity);
    public void update(T entity);
    public List<T> getAllData();
    //public void addEntity(T entity);
    public void removeEntity(T entity) ;
    public void updateEntity(T entity) ;
    public void addEntity(T t);
    public void deleteEntity(int id,T t);
    public void updateEntity(int id,T t);
    public List<T> getallData();
}
