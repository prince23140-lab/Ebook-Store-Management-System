// Payment.java
package auca.ac.rw.ebook.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID payment_id;

    // MANY-TO-ONE: Many Payments can belong to one Order (One-to-One in business logic)
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // MANY-TO-ONE: Many Payments can be made by one User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private EPaymentMethod method;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private EPaymentStatus status;

    private LocalDateTime payment_date;

    // Getters and setters
    public UUID getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(UUID payment_id) {
        this.payment_id = payment_id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EPaymentMethod getMethod() {
        return method;
    }

    public void setMethod(EPaymentMethod method) {
        this.method = method;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public EPaymentStatus getStatus() {
        return status;
    }

    public void setStatus(EPaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(LocalDateTime payment_date) {
        this.payment_date = payment_date;
    }
}