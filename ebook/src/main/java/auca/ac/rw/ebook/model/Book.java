// Book.java
package auca.ac.rw.ebook.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID book_id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    // MANY-TO-ONE: Many Books can belong to one Category
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private BigDecimal price;

    private Integer stock_quantity;
    private String cover_image;
    private String file_url;

    // ONE-TO-MANY: One Book can be in multiple Cart items
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Cart> cartItems;

    // ONE-TO-MANY: One Book can be in multiple OrderDetails
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    // ONE-TO-MANY: One Book can have multiple Reviews
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Review> reviews;

    // Getters and setters
    public UUID getBook_id() {
        return book_id;
    }

    public void setBook_id(UUID book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(Integer stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public List<Cart> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<Cart> cartItems) {
        this.cartItems = cartItems;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}