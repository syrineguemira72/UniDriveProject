package edu.unidrive.entities;

public class Post {
    private int id;
    private String title;
    private String description;
    private String category; // Ajout du champ category
    private int userId; // Ajout de l'ID utilisateur

    // Ajoutez le getter et setter
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Post() {

    }

    public Post(int id, String description, String title) {
        this.id = id;
        this.description = description;
        this.title = title;
    }

    public Post(String description, String title) {
        this.description = description;
        this.title = title;
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
