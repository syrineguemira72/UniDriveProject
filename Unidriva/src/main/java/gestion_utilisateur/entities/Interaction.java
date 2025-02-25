package gestion_forum.entities;

import java.time.LocalDate;

public class Interaction {
    private int id;
    private String content;
    private LocalDate date;
    private int postId;

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
