// LocationService.java
package auca.ac.rw.ebook.service;

import auca.ac.rw.ebook.model.Location;
import auca.ac.rw.ebook.repository.LocationRepository;
import auca.ac.rw.ebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LocationService {
    
    @Autowired
    private LocationRepository locationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // CRUD Operations
    public String saveLocation(Location location) {
        if (!locationRepository.existsByProvinceAndDistrictAndSectorAndCellAndVillage(
            location.getProvince(), location.getDistrict(), location.getSector(), 
            location.getCell(), location.getVillage())) {
            locationRepository.save(location);
            return "Location saved successfully";
        } else {
            return "Location already exists";
        }
    }
    
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
    
    public Optional<Location> getLocationById(UUID id) {
        return locationRepository.findById(id);
    }
    
    public String updateLocation(Location location) {
        if (locationRepository.existsById(location.getLocation_id())) {
            locationRepository.save(location);
            return "Location updated successfully";
        } else {
            return "Location not found";
        }
    }
    
    public String deleteLocation(UUID id) {
        if (locationRepository.existsById(id)) {
            locationRepository.deleteById(id);
            return "Location deleted successfully";
        } else {
            return "Location not found";
        }
    }
    
    // findBy... methods
    public List<Location> getLocationsByProvince(String province) {
        return locationRepository.findByProvince(province);
    }
    
    public List<Location> getLocationsByDistrict(String district) {
        return locationRepository.findByDistrict(district);
    }
    
    public List<Location> getLocationsBySector(String sector) {
        return locationRepository.findBySector(sector);
    }
    
    public List<Location> getLocationsByCell(String cell) {
        return locationRepository.findByCell(cell);
    }
    
    public List<Location> getLocationsByVillage(String village) {
        return locationRepository.findByVillage(village);
    }
    
    public Optional<Location> getLocationByFullAddress(String province, String district, String sector, String cell, String village) {
        return locationRepository.findByProvinceAndDistrictAndSectorAndCellAndVillage(province, district, sector, cell, village);
    }
    
    // existsBy... methods
    public Boolean checkLocationExists(String province, String district, String sector, String cell, String village) {
        return locationRepository.existsByProvinceAndDistrictAndSectorAndCellAndVillage(province, district, sector, cell, village);
    }
    
    public Boolean checkProvinceExists(String province) {
        return locationRepository.existsByProvince(province);
    }
    
    public Boolean checkDistrictExists(String district) {
        return locationRepository.existsByDistrict(district);
    }
    
    // Sorting and Pagination
    public Page<Location> getLocationsByProvinceWithPagination(String province, Pageable pageable) {
        return locationRepository.findByProvince(province, pageable);
    }
    
    public Page<Location> getLocationsByDistrictWithPagination(String district, Pageable pageable) {
        return locationRepository.findByDistrict(district, pageable);
    }
    
    public Page<Location> getAllLocationsWithPagination(Pageable pageable) {
        return locationRepository.findAll(pageable);
    }
    
    // Midterm Requirement: Get users by province
    public List<Object[]> getUsersByProvince(String province) {
        return userRepository.findByProvince(province).stream()
            .map(user -> new Object[]{user, user.getLocation()})
            .toList();
    }
    
    public List<Object[]> getUsersByDistrict(String district) {
        return userRepository.findByDistrict(district).stream()
            .map(user -> new Object[]{user, user.getLocation()})
            .toList();
    }
    
    // Custom queries for Rwandan hierarchy
    public List<String> getAllProvinces() {
        return locationRepository.findAllDistinctProvinces();
    }
    
    public List<Location> getLocationsByProvinceAndDistrict(String province, String district) {
        return locationRepository.findByProvinceAndDistrict(province, district);
    }
}