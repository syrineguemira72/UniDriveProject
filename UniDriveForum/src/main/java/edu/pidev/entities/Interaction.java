package edu.pidev.entities;

import java.time.LocalDate;

public class Interaction {
    private int id;
    private String content; // Texte du commentaire ou null pour les réactions
    private LocalDate date; // Date de l'interaction
    private int postId; // ID du post associé

    public Interaction() {
    }

    @Override
    public String toString() {
        return "Interaction{" +
                "id=" + id +

                ", content='" + content + '\'' +
                ", date=" + date +
                ", postId=" + postId +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }






    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Interaction(int id, String content, LocalDate date, int postId) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.postId = postId;
    }

    public Interaction(String content, LocalDate date, int postId) {

       this.content = content;
        this.date = date;
        this.postId = postId;
    }
}
