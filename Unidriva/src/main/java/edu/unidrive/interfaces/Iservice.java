package edu.unidrive.interfaces;

import java.util.List;

public interface Iservice<T> {
    public void add(T entity);
    public void remove(T entity);
    public void update(T entity);
    public List<T> getAllData();
}
