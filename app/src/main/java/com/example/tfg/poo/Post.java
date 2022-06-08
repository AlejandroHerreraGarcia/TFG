package com.example.tfg.poo;

public class Post {

    private String id;
    private String title;
    private String description;
    private String fecha;
    private String idUser;
    private String category;
    private String location;
    private long time;

    public Post() {

    }

    public Post(String id, String title, String description, String fecha, String idUser, String category, String location, long time) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fecha = fecha;
        this.idUser = idUser;
        this.category = category;
        this.location = location;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
