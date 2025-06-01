package ru.slava.customer_app.entity;

import java.util.UUID;

public class ProductReview {
    private UUID id;

    private int productId;

    private int rating;

    private String review;

    public ProductReview() {
    }

    public ProductReview(UUID id, int productId, int rating, String review) {
        this.id = id;
        this.productId = productId;
        this.rating = rating;
        this.review = review;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + productId;
        result = prime * result + rating;
        result = prime * result + ((review == null) ? 0 : review.hashCode());
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
        ProductReview other = (ProductReview) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (productId != other.productId)
            return false;
        if (rating != other.rating)
            return false;
        if (review == null) {
            if (other.review != null)
                return false;
        } else if (!review.equals(other.review))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ProductReview [id=" + id + ", productId=" + productId + ", rating=" + rating + ", review=" + review
                + "]";
    }
}
