package gestion_utilisateur.interfaces;

import java.util.List;

public interface Iservice<T> {
    public void addEntity(T t);
    public void deleteEntity(int id,T t);
    public void updateEntity(int id,T t);
    public List<T> getallData();

}
