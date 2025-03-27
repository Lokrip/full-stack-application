package ru.slava.manager_app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_authority", schema = "user_management")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "c_authority")
    private String autority;

    public Authority(Integer id, String autority) {
        this.id = id;
        this.autority = autority;
    }

    public Authority() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAutority() {
        return autority;
    }

    public void setAutority(String autority) {
        this.autority = autority;
    }

    @Override
    public String toString() {
        return "Authority [id=" + id + ", autority=" + autority + "]";
    }
}
