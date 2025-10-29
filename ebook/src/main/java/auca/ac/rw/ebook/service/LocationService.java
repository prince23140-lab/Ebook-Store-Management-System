package auca.ac.rw.ebook.service;

import auca.ac.rw.ebook.model.Location;
import auca.ac.rw.ebook.model.ELocationType;
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
    
    // CRUD Operations with parent-child support
    public String saveLocation(String parentCode, Location location) {
        if (parentCode != null) {
            Optional<Location> parent = locationRepository.findByCode(parentCode);
            if (parent.isPresent()) {
                // Validate hierarchy: Province → District → Sector → Cell → Village
                if (!isValidHierarchy(parent.get().getType(), location.getType())) {
                    return "Invalid location hierarchy";
                }
                
                location.setParent(parent.get());
                if (!locationRepository.existsByCode(location.getCode())) {
                    locationRepository.save(location);
                    return location.getType() + " saved successfully under parent " + parentCode;
                } else {
                    return "Location with this code already exists";
                }
            } else {
                return "Parent location not found";
            }
        } else {
            // Root level (Provinces) must have no parent
            if (location.getType() != ELocationType.PROVINCE) {
                return "Only provinces can be created without a parent";
            }
            if (!locationRepository.existsByCode(location.getCode())) {
                locationRepository.save(location);
                return "Province saved successfully";
            } else {
                return "Location with this code already exists";
            }
        }
    }
    
    private boolean isValidHierarchy(ELocationType parentType, ELocationType childType) {
        switch (parentType) {
            case PROVINCE: return childType == ELocationType.DISTRICT;
            case DISTRICT: return childType == ELocationType.SECTOR;
            case SECTOR: return childType == ELocationType.CELL;
            case CELL: return childType == ELocationType.VILLAGE;
            default: return false;
        }
    }
    
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }
    
    public Optional<Location> getLocationById(UUID id) {
        return locationRepository.findById(id);
    }
    
    public Optional<Location> getLocationByCode(String code) {
        return locationRepository.findByCode(code);
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
            // Check if location has children
            Optional<Location> location = locationRepository.findById(id);
            if (location.isPresent() && !location.get().getChildren().isEmpty()) {
                return "Cannot delete location: It has child locations";
            }
            locationRepository.deleteById(id);
            return "Location deleted successfully";
        } else {
            return "Location not found";
        }
    }
    
    // Hierarchy queries
    public List<Location> getProvinces() {
        return locationRepository.findAllProvinces();
    }
    
    public List<Location> getDistrictsByProvince(String provinceCode) {
        return locationRepository.findDistrictsByProvinceCode(provinceCode);
    }
    
    public List<Location> getSectorsByDistrict(String districtCode) {
        return locationRepository.findSectorsByDistrictCode(districtCode);
    }
    
    public List<Location> getCellsBySector(String sectorCode) {
        return locationRepository.findCellsBySectorCode(sectorCode);
    }
    
    public List<Location> getVillagesByCell(String cellCode) {
        return locationRepository.findVillagesByCellCode(cellCode);
    }
    
    public List<Location> getChildrenByParentCode(String parentCode) {
        return locationRepository.findByParentCode(parentCode);
    }
    
    public List<Location> getChildrenByParentId(UUID parentId) {
        return locationRepository.findByParentId(parentId);
    }
    
    // Midterm Requirement: Get users by province
    public List<Object[]> getUsersByProvinceCode(String provinceCode) {
        return locationRepository.findUsersByProvinceCode(provinceCode);
    }
    
    public List<Object[]> getUsersByProvinceName(String provinceName) {
        return locationRepository.findUsersByProvinceName(provinceName);
    }
    
    // Full hierarchy traversal
    public String getFullLocationPath(String locationCode) {
        Optional<Location> location = locationRepository.findByCode(locationCode);
        if (location.isPresent()) {
            return buildLocationPath(location.get());
        }
        return "Location not found";
    }
    
    private String buildLocationPath(Location location) {
        if (location.getParent() == null) {
            return location.getName();
        }
        return buildLocationPath(location.getParent()) + " → " + location.getName();
    }
    
    // Sorting and Pagination
    public Page<Location> getLocationsByTypeWithPagination(ELocationType type, Pageable pageable) {
        return locationRepository.findByType(type, pageable);
    }
    
    public Page<Location> getProvincesWithPagination(Pageable pageable) {
        return locationRepository.findByParentIsNull(pageable);
    }
}