// User.java - Fix the location relationship
package auca.ac.rw.ebook.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID user_id;

    @Column(nullable = false)
    private String full_name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;

    @Enumerated(EnumType.STRING)
    private EUserRole role;

    // FIXED: Remove @JsonIgnore to see location in response
    @ManyToOne(fetch = FetchType.EAGER) // Changed to EAGER loading
    @JoinColumn(name = "location_id")
    private Location location;

    private LocalDateTime created_at;

    // Other relationships remain with @JsonIgnore
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cart> cartItems;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;

    // Getters and setters
    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public EUserRole getRole() {
        return role;
    }

    public void setRole(EUserRole role) {
        this.role = role;
    }

    // FIXED: Proper getter and setter for location
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public List<Cart> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<Cart> cartItems) {
        this.cartItems = cartItems;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    // Helper method to get user's full location path
    public String getFullLocationPath() {
        if (location == null) {
            return "No location assigned";
        }
        return buildLocationPath(location);
    }

    private String buildLocationPath(Location location) {
        if (location.getParent() == null) {
            return location.getName();
        }
        return buildLocationPath(location.getParent()) + " → " + location.getName();
    }

    // Helper method to get user's province
    public String getProvince() {
        if (location == null) return null;
        
        Location current = location;
        while (current != null) {
            if (current.getType() == ELocationType.PROVINCE) {
                return current.getName();
            }
            current = current.getParent();
        }
        return null;
    }

    // Helper method to get user's district
    public String getDistrict() {
        if (location == null) return null;
        
        Location current = location;
        while (current != null) {
            if (current.getType() == ELocationType.DISTRICT) {
                return current.getName();
            }
            current = current.getParent();
        }
        return null;
    }

    // Helper method to get user's sector
    public String getSector() {
        if (location == null) return null;
        
        Location current = location;
        while (current != null) {
            if (current.getType() == ELocationType.SECTOR) {
                return current.getName();
            }
            current = current.getParent();
        }
        return null;
    }

    // Helper method to get user's cell
    public String getCell() {
        if (location == null) return null;
        
        Location current = location;
        while (current != null) {
            if (current.getType() == ELocationType.CELL) {
                return current.getName();
            }
            current = current.getParent();
        }
        return null;
    }
}