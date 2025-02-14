package ru.slava.manager_app.entity;

public class Product {
    private Integer id;
    private String title;
    private String details;

    
    public Product(Integer id, String title, String details) {
        this.id = id;
        this.title = title;
        this.details = details;
    }

    public Product() {
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }

    
}
