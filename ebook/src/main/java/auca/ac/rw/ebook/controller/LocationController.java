// LocationController.java
package auca.ac.rw.ebook.controller;

import auca.ac.rw.ebook.model.Location;
import auca.ac.rw.ebook.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    
    @Autowired
    private LocationService locationService;
    
    // CRUD Endpoints
    @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, 
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveLocation(@RequestBody Location location) {
        String response = locationService.saveLocation(location);
        if (response.equals("Location saved successfully")) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }
    
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLocationById(@PathVariable UUID id) {
        Optional<Location> location = locationService.getLocationById(id);
        if (location.isPresent()) {
            return new ResponseEntity<>(location.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Location not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateLocation(@RequestBody Location location) {
        String response = locationService.updateLocation(location);
        if (response.equals("Location updated successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable UUID id) {
        String response = locationService.deleteLocation(id);
        if (response.equals("Location deleted successfully")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    // findBy... Endpoints
    @GetMapping(value = "/province/{province}")
    public ResponseEntity<List<Location>> getLocationsByProvince(@PathVariable String province) {
        List<Location> locations = locationService.getLocationsByProvince(province);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
    
    @GetMapping(value = "/district/{district}")
    public ResponseEntity<List<Location>> getLocationsByDistrict(@PathVariable String district) {
        List<Location> locations = locationService.getLocationsByDistrict(district);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
    
    // existsBy... Endpoints
    @GetMapping(value = "/exists")
    public ResponseEntity<Boolean> checkLocationExists(
            @RequestParam String province, 
            @RequestParam String district,
            @RequestParam String sector,
            @RequestParam String cell,
            @RequestParam String village) {
        Boolean exists = locationService.checkLocationExists(province, district, sector, cell, village);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    // Sorting and Pagination Endpoints
    @GetMapping(value = "/province/{province}/page")
    public ResponseEntity<Page<Location>> getLocationsByProvinceWithPagination(
            @PathVariable String province,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "village") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Location> locations = locationService.getLocationsByProvinceWithPagination(province, pageable);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }
    
    // Midterm Requirement: Get users by province
    @GetMapping(value = "/users/by-province/{province}")
    public ResponseEntity<?> getUsersByProvince(@PathVariable String province) {
        List<Object[]> usersWithLocations = locationService.getUsersByProvince(province);
        return new ResponseEntity<>(usersWithLocations, HttpStatus.OK);
    }
    
    @GetMapping(value = "/users/by-district/{district}")
    public ResponseEntity<?> getUsersByDistrict(@PathVariable String district) {
        List<Object[]> usersWithLocations = locationService.getUsersByDistrict(district);
        return new ResponseEntity<>(usersWithLocations, HttpStatus.OK);
    }
    
    // Additional endpoints for Rwandan hierarchy
    @GetMapping(value = "/provinces")
    public ResponseEntity<List<String>> getAllProvinces() {
        // This would require a custom method in service/repository
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}