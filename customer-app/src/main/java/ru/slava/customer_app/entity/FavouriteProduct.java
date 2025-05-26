package ru.slava.customer_app.entity;

import java.util.UUID;

public class FavouriteProduct {
    private UUID id;

    private int productid;

    public FavouriteProduct() {
    }

    public FavouriteProduct(UUID id, int productid) {
        this.id = id;
        this.productid = productid;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    @Override
    public String toString() {
        return "FavouriteProduct [id=" + id + ", productid=" + productid + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + productid;
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
        FavouriteProduct other = (FavouriteProduct) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (productid != other.productid)
            return false;
        return true;
    }
}
