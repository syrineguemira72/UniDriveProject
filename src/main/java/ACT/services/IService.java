package ACT.services;

import java.sql.SQLException;
import java.util.List;

public interface IService<A> {
    void ajouter(A a) throws SQLException;

    void modifier(A a) throws SQLException;



    void supprimer(A a) throws SQLException;


    void supprimer(int id) throws SQLException;

    List<A> afficher() throws SQLException;
}
