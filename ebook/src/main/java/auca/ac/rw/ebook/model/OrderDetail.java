// OrderDetail.java
package auca.ac.rw.ebook.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID detail_id;

    // MANY-TO-ONE: Many OrderDetails can belong to one Order
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // MANY-TO-ONE: Many OrderDetails can reference one Book
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private Integer quantity;
    private BigDecimal price;

    // Getters and setters
    public UUID getDetail_id() {
        return detail_id;
    }

    public void setDetail_id(UUID detail_id) {
        this.detail_id = detail_id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}