package ru.slava.catalogue.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(schema = "catalogue", name = "t_product")
//можно писать свой имменованые sql запросы
//В данном коде используется аннотация @NamedQueries, которая
//позволяет задать именованные запросы (Named Queries) в JPA (Java Persistence API).
//Однако сам запрос "Product.findAllByTitleLikeIgnoringCase"
@NamedQueries(
    //каждый именнованый запрос должен иметь уникальное название в рамках всего приложения
    @NamedQuery(
        name = "Product.findAllByTitleLikeIgnoringCase",
        query = "select p from Product p where p.title ilike :filter"
    )
)
public class Product {
    public Product() {
    }

    public Product(Integer id, String title, String details) {
        this.id = id;
        this.title = title;
        this.details = details;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "c_title")
    //эти правила валидаций будут сробатывать моменте сохронения или загрузки данных в базе данных
    @NotNull(message = "{catalogue.errors.product.create.errors.title_is_null}")
    @Size(min = 3, max = 50, message = "{catalogue.errors.product.create.errors.title_size_is_invalid}")
    private String title;

    @Column(name = "c_details")
    @Size(max = 550, message = "{catalogue.errors.product.create.errors.details_size_is_invalid}")
    private String details;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((details == null) ? 0 : details.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (details == null) {
            if (other.details != null)
                return false;
        } else if (!details.equals(other.details))
            return false;
        return true;
    }
}
