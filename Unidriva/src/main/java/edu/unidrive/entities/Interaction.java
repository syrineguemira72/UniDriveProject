package edu.unidrive.entities;

import java.time.LocalDateTime;
public class Interaction {
    private int id;
    private String content;
    private LocalDateTime created_at;  // Changed from date to createdAt
    private int postId;
    private int user_idc;

    public Interaction() {
    }

    @Override
    public String toString() {
        return "Interaction{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", created_at=" + created_at +
                ", postId=" + postId +
                ", user_idc=" + user_idc +
                '}';
    }
    // Getters and setters
    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(LocalDateTime created_at) {
        this.created_at = created_at;
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

    public int getUser_idc() {
        return user_idc;
    }

    public void setUser_idc(int userId) {
        this.user_idc = user_idc;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public Interaction(int id, String content, LocalDateTime created_at, int postId, int user_idc) {
        this.id = id;
        this.content = content;
        this.created_at = created_at;
        this.postId = postId;
        this.user_idc = user_idc;
    }

    public Interaction(String content, LocalDateTime created_at, int postId, int user_idc) {

        this.content = content;
        this.created_at = created_at;
        this.postId = postId;
        this.user_idc = user_idc;
    }
}
