// Review.java
package auca.ac.rw.ebook.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID review_id;

    // MANY-TO-ONE: Many Reviews can be written by one User
    // REMOVED: @JsonIgnore - this was preventing deserialization
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // MANY-TO-ONE: Many Reviews can be written for one Book
    // REMOVED: @JsonIgnore - this was preventing deserialization
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private Book book;

    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private LocalDateTime created_at;

    // Getters and setters
    public UUID getReview_id() {
        return review_id;
    }

    public void setReview_id(UUID review_id) {
        this.review_id = review_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}