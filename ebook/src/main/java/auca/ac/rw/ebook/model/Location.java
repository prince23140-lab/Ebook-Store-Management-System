package auca.ac.rw.ebook.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID location_id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private ELocationType type;

    // PARENT-CHILD RELATIONSHIP: Many locations can have one parent
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Location parent;

    // PARENT-CHILD RELATIONSHIP: One location can have many children
    @JsonIgnore
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Location> children;

    // ONE-TO-MANY: One Location can have multiple Users
    @JsonIgnore
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<User> users;

    // Getters and setters
    public UUID getLocation_id() {
        return location_id;
    }

    public void setLocation_id(UUID location_id) {
        this.location_id = location_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ELocationType getType() {
        return type;
    }

    public void setType(ELocationType type) {
        this.type = type;
    }

    public Location getParent() {
        return parent;
    }

    public void setParent(Location parent) {
        this.parent = parent;
    }

    public List<Location> getChildren() {
        return children;
    }

    public void setChildren(List<Location> children) {
        this.children = children;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}