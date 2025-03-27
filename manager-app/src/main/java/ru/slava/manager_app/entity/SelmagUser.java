package ru.slava.manager_app.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_user", schema = "user_management")
public class SelmagUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "c_username")
    private String username;

    @Column(name = "c_password")
    private String password;

    @ManyToMany
    @JoinTable(
        schema = "user_management",
        name = "t_user_authority",
        joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_authority", referencedColumnName = "id")
    )
    private List<Authority> autorities;

    public SelmagUser(Integer id, String username, String password, List<Authority> autorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.autorities = autorities;
    }

    public SelmagUser() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Authority> getAutorities() {
        return autorities;
    }

    public void setAutorities(List<Authority> autorities) {
        this.autorities = autorities;
    }
}
