package gestion_forum.interfaces;
package gestion_utilisateur.interfaces;

import java.util.List;

public interface Iservice<T> {
    public void addEntity(T entity);
    public void removeEntity(T entity) ;
    public void updateEntity(T entity) ;
    public List<T> getAllData();
    public void addEntity(T t);
    public void deleteEntity(int id,T t);
    public void updateEntity(int id,T t);
    public List<T> getallData();

}
