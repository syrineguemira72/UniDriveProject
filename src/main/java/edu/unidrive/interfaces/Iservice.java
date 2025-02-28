package edu.unidrive.interfaces;

import java.util.List;

public interface Iservice<T> {

    public void addEntity(T t);
    public void updateEntity(int id, T t);
    public void deleteEntity(T t);
    public List<T> getAllData();
    public T getEntity(int id);

}
