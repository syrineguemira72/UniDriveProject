package gestion_forum.tests;

import gestion_forum.entities.Post;
import gestion_forum.services.PostService;
import gestion_forum.tools.MyConnection;


public class MainClass {
    public static void main(String[] args) {
        MyConnection mc = new MyConnection();
        PostService ps = new PostService();
        Post p1 = new Post("MARWA","SS");
       ps.addEntity(p1);
        System.out.println("Ajouté avec ID : " + p1.getId());
        p1.setDescription("cc");
      p1.setTitle("syiii");
      ps.updateEntity(p1);
        ps.removeEntity(p1);
       System.out.println(ps.getAllData());
        /*InteractionService ts = new InteractionService();
        Interaction t1 = new Interaction("J4AIME ESPRIT",java.time.LocalDate.now(),54);
       ts.addEntity(t1);
        System.out.println("Ajouté avec ID : " + t1.getId());
       t1.setContent("jedetesteesprit");
        t1.setDate(java.time.LocalDate.now());
       ts.updateEntity(t1);
       // ts.removeEntity(t1);
        System.out.println(ts.getAllData());*/



    }
}

