// Location.java
package auca.ac.rw.ebook.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID location_id;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String sector;

    @Column(nullable = false)
    private String cell;

    @Column(nullable = false)
    private String village;

    // ONE-TO-MANY: One Location can have multiple Users
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<User> users;

    // Getters and setters
    public UUID getLocation_id() {
        return location_id;
    }

    public void setLocation_id(UUID location_id) {
        this.location_id = location_id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}