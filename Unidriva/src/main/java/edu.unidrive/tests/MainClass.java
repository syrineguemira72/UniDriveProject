package edu.unidrive.tests;

import edu.unidrive.entities.Trajet;
import edu.unidrive.entities.Reservation;
import edu.unidrive.services.ReservationService;
import edu.unidrive.tools.MyConnection;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static edu.unidrive.entities.Etat.En_attente;

public class MainClass {


    public static void main(String[] args) {
        MyConnection connection = MyConnection.getInstance();

        Trajet t = new Trajet(1,"lac","Ariana Soghra", LocalDateTime.now(), 25, 11, 3);
        Trajet t1 = new Trajet(2,"manouba","Ariana Soghra", LocalDateTime.now(), 35, 15, 4);
        Trajet t2 = new Trajet(3,"Bardo","Ariana Soghra", LocalDateTime.now(), 30, 11, 3);
        Trajet t3 = new Trajet(4,"manouba","Ariana Soghra", LocalDateTime.now(), 35, 15, 4);

        //TrajetService ts = new TrajetService();
        //ts.addEntity(t);
        //ts.addEntity(t1);
        //ts.addEntity(t2);
        //ts.addEntity(t3);

        //liste des trajet disponibles
        //ts.deleteEntity(t3);
        //System.out.println(ts.getAllData());
        //System.out.println(ts.getAllData());
        //System.out.println(ts.getEntity(t.getId()));
        //ts.updateEntity(3,t2);


        Reservation r0 = new Reservation(1,t,En_attente, LocalDate.now());
        Reservation r1 = new Reservation(2,t1,En_attente, LocalDate.now());
        Reservation r2 = new Reservation(3,t2,En_attente, LocalDate.now());
        Reservation r3 = new Reservation(4,t3,En_attente, LocalDate.now());

        ReservationService res = new ReservationService();

        //res.addEntity(r0);
        //res.addEntity(r1);
        //res.addEntity(r2);
        //res.addEntity(r3);


        //System.out.println(res.getAllData());
        //System.out.println(res.getEntity(r0.getId()));

        //res.updateEntity(r1.getId(),r1);

        //res.deleteEntity(r3);
        //res.deleteEntity(r2);

    }
}
